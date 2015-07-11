/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.media.cts;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.media.cts;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.opengl.GLES20;
import android.test.AndroidTestCase;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.microedition.khronos.opengles.GL10;
/**
 * Generates a series of video frames, encodes them, decodes them, and tests for significant
 * divergence from the original.
 * <p>
 * We copy the data from the encoder's output buffers to the decoder's input buffers, running
 * them in parallel.  The first buffer output for video/avc contains codec configuration data,
 * which we must carefully forward to the decoder.
 * <p>
 * An alternative approach would be to save the output of the decoder as an mpeg4 video
 * file, and read it back in from disk.  The data we're generating is just an elementary
 * stream, so we'd need to perform additional steps to make that happen.
 */
public class EncodeDecodeTest extends AndroidTestCase {
    private static final String TAG = "EncodeDecodeTest";
    private static final boolean VERBOSE = false;           // lots of logging
    private static final boolean DEBUG_SAVE_FILE = false;   // save copy of encoded movie
    private static final String DEBUG_FILE_NAME_BASE = "/sdcard/test.";
    // parameters for the encoder
    private static final String MIME_TYPE = "video/avc";    // H.264 Advanced Video Coding
    private static final int FRAME_RATE = 15;               // 15fps
    private static final int IFRAME_INTERVAL = 10;          // 10 seconds between I-frames
    // movie length, in frames
    private static final int NUM_FRAMES = 30;               // two seconds of video
    private static final int TEST_Y = 120;                  // YUV values for colored rect
    private static final int TEST_U = 160;
    private static final int TEST_V = 200;
    private static final int TEST_R0 = 0;                   // RGB equivalent of {0,0,0}
    private static final int TEST_G0 = 136;
    private static final int TEST_B0 = 0;
    private static final int TEST_R1 = 236;                 // RGB equivalent of {120,160,200}
    private static final int TEST_G1 = 50;
    private static final int TEST_B1 = 186;
    // size of a frame, in pixels
    private int mWidth = -1;
    private int mHeight = -1;
    // bit rate, in bits per second
    private int mBitRate = -1;
    // largest color component delta seen (i.e. actual vs. expected)
    private int mLargestColorDelta;
    /**
     * Tests streaming of AVC video through the encoder and decoder.  Data is encoded from
     * a series of byte[] buffers and decoded into ByteBuffers.  The output is checked for
     * validity.
     */
    public void testEncodeDecodeVideoFromBufferToBufferQCIF() throws Exception {
        setParameters(176, 144, 1000000);
        encodeDecodeVideoFromBuffer(false);
    }
    public void testEncodeDecodeVideoFromBufferToBufferQVGA() throws Exception {
        setParameters(320, 240, 2000000);
        encodeDecodeVideoFromBuffer(false);
    }
    public void testEncodeDecodeVideoFromBufferToBuffer720p() throws Exception {
        setParameters(1280, 720, 6000000);
        encodeDecodeVideoFromBuffer(false);
    }
    /**
     * Tests streaming of AVC video through the encoder and decoder.  Data is encoded from
     * a series of byte[] buffers and decoded into Surfaces.  The output is checked for
     * validity.
     * <p>
     * Because of the way SurfaceTexture.OnFrameAvailableListener works, we need to run this
     * test on a thread that doesn't have a Looper configured.  If we don't, the test will
     * pass, but we won't actually test the output because we'll never receive the "frame
     * available" notifications".  The CTS test framework seems to be configuring a Looper on
     * the test thread, so we have to hand control off to a new thread for the duration of
     * the test.
     */
    public void testEncodeDecodeVideoFromBufferToSurfaceQCIF() throws Throwable {
        setParameters(176, 144, 1000000);
        BufferToSurfaceWrapper.runTest(this);
    }
    public void testEncodeDecodeVideoFromBufferToSurfaceQVGA() throws Throwable {
        setParameters(320, 240, 2000000);
        BufferToSurfaceWrapper.runTest(this);
    }
    public void testEncodeDecodeVideoFromBufferToSurface720p() throws Throwable {
        setParameters(1280, 720, 6000000);
        BufferToSurfaceWrapper.runTest(this);
    }
    /** Wraps testEncodeDecodeVideoFromBuffer(true) */
    private static class BufferToSurfaceWrapper implements Runnable {
        private Throwable mThrowable;
        private EncodeDecodeTest mTest;
        private BufferToSurfaceWrapper(EncodeDecodeTest test) {
            mTest = test;
        }
        @Override
        public void run() {
            try {
                mTest.encodeDecodeVideoFromBuffer(true);
            } catch (Throwable th) {
                mThrowable = th;
            }
        }
        /**
         * Entry point.
         */
        public static void runTest(EncodeDecodeTest obj) throws Throwable {
            BufferToSurfaceWrapper wrapper = new BufferToSurfaceWrapper(obj);
            Thread th = new Thread(wrapper, "codec test");
            th.start();
            th.join();
            if (wrapper.mThrowable != null) {
                throw wrapper.mThrowable;
            }
        }
    }
    /**
     * Tests streaming of AVC video through the encoder and decoder.  Data is provided through
     * a Surface and decoded onto a Surface.  The output is checked for validity.
     */
    public void testEncodeDecodeVideoFromSurfaceToSurfaceQCIF() throws Throwable {
        setParameters(176, 144, 1000000);
        SurfaceToSurfaceWrapper.runTest(this);
    }
    public void testEncodeDecodeVideoFromSurfaceToSurfaceQVGA() throws Throwable {
        setParameters(320, 240, 2000000);
        SurfaceToSurfaceWrapper.runTest(this);
    }
    public void testEncodeDecodeVideoFromSurfaceToSurface720p() throws Throwable {
        setParameters(1280, 720, 6000000);
        SurfaceToSurfaceWrapper.runTest(this);
    }
    /** Wraps testEncodeDecodeVideoFromSurfaceToSurface() */
    private static class SurfaceToSurfaceWrapper implements Runnable {
        private Throwable mThrowable;
        private EncodeDecodeTest mTest;
        private SurfaceToSurfaceWrapper(EncodeDecodeTest test) {
            mTest = test;
        }
        @Override
        public void run() {
            try {
                mTest.encodeDecodeVideoFromSurfaceToSurface();
            } catch (Throwable th) {
                mThrowable = th;
            }
        }
        /**
         * Entry point.
         */
        public static void runTest(EncodeDecodeTest obj) throws Throwable {
            SurfaceToSurfaceWrapper wrapper = new SurfaceToSurfaceWrapper(obj);
            Thread th = new Thread(wrapper, "codec test");
            th.start();
            th.join();
            if (wrapper.mThrowable != null) {
                throw wrapper.mThrowable;
            }
        }
    }
    /**
     * Sets the desired frame size and bit rate.
     */
    private void setParameters(int width, int height, int bitRate) {
        if ((width % 16) != 0 || (height % 16) != 0) {
            Log.w(TAG, "WARNING: width or height not multiple of 16");
        }
        mWidth = width;
        mHeight = height;
        mBitRate = bitRate;
    }
    /**
     * Tests encoding and subsequently decoding video from frames generated into a buffer.
     * <p>
     * We encode several frames of a video test pattern using MediaCodec, then decode the
     * output with MediaCodec and do some simple checks.
     * <p>
     * See http://b.android.com/37769 for a discussion of input format pitfalls.
     */
    private void encodeDecodeVideoFromBuffer(boolean toSurface) throws Exception {
        MediaCodec encoder = null;
        MediaCodec decoder = null;
        mLargestColorDelta = -1;
        try {
            MediaCodecInfo codecInfo = selectCodec(MIME_TYPE);
            if (codecInfo == null) {
                // Don't fail CTS if they don't have an AVC codec (not here, anyway).
                Log.e(TAG, "Unable to find an appropriate codec for " + MIME_TYPE);
                return;
            }
            if (VERBOSE) Log.d(TAG, "found codec: " + codecInfo.getName());
            int colorFormat = selectColorFormat(codecInfo, MIME_TYPE);
            if (VERBOSE) Log.d(TAG, "found colorFormat: " + colorFormat);
            // We avoid the device-specific limitations on width and height by using values that
            // are multiples of 16, which all tested devices seem to be able to handle.
            MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, mWidth, mHeight);
            // Set some properties.  Failing to specify some of these can cause the MediaCodec
            // configure() call to throw an unhelpful exception.
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat);
            format.setInteger(MediaFormat.KEY_BIT_RATE, mBitRate);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);
            if (VERBOSE) Log.d(TAG, "format: " + format);
            // Create a MediaCodec for the desired codec, then configure it as an encoder with
            // our desired properties.
            encoder = MediaCodec.createByCodecName(codecInfo.getName());
            encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            encoder.start();
            // Create a MediaCodec for the decoder, just based on the MIME type.  The various
            // format details will be passed through the csd-0 meta-data later on.
            decoder = MediaCodec.createDecoderByType(MIME_TYPE);
            doEncodeDecodeVideoFromBuffer(encoder, colorFormat, decoder, toSurface);
        } finally {
            if (VERBOSE) Log.d(TAG, "releasing codecs");
            if (encoder != null) {
                encoder.stop();
                encoder.release();
            }
            if (decoder != null) {
                decoder.stop();
                decoder.release();
            }
            Log.i(TAG, "Largest color delta: " + mLargestColorDelta);
        }
    }
    /**
     * Tests encoding and subsequently decoding video from frames generated into a buffer.
     * <p>
     * We encode several frames of a video test pattern using MediaCodec, then decode the
     * output with MediaCodec and do some simple checks.
     */
    private void encodeDecodeVideoFromSurfaceToSurface() throws Exception {
        MediaCodec encoder = null;
        MediaCodec decoder = null;
        InputSurface inputSurface = null;
        OutputSurface outputSurface = null;
        mLargestColorDelta = -1;
        try {
            MediaCodecInfo codecInfo = selectCodec(MIME_TYPE);
            if (codecInfo == null) {
                // Don't fail CTS if they don't have an AVC codec (not here, anyway).
                Log.e(TAG, "Unable to find an appropriate codec for " + MIME_TYPE);
                return;
            }
            if (VERBOSE) Log.d(TAG, "found codec: " + codecInfo.getName());
            int colorFormat = MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface;
            // We avoid the device-specific limitations on width and height by using values that
            // are multiples of 16, which all tested devices seem to be able to handle.
            MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, mWidth, mHeight);
            // Set some properties.  Failing to specify some of these can cause the MediaCodec
            // configure() call to throw an unhelpful exception.
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat);
            format.setInteger(MediaFormat.KEY_BIT_RATE, mBitRate);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);
            if (VERBOSE) Log.d(TAG, "format: " + format);
            // Create the output surface.
            outputSurface = new OutputSurface(mWidth, mHeight);
            // Create a MediaCodec for the decoder, just based on the MIME type.  The various
            // format details will be passed through the csd-0 meta-data later on.
            decoder = MediaCodec.createDecoderByType(MIME_TYPE);
            MediaFormat decoderFormat = MediaFormat.createVideoFormat(MIME_TYPE, mWidth, mHeight);
            decoder.configure(format, outputSurface.getSurface(), null, 0);
            decoder.start();
            // Create a MediaCodec for the desired codec, then configure it as an encoder with
            // our desired properties.  Request a Surface to use for input.
            encoder = MediaCodec.createByCodecName(codecInfo.getName());
            encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            inputSurface = new InputSurface(encoder.createInputSurface());
            encoder.start();
            doEncodeDecodeVideoFromSurfaceToSurface(encoder, inputSurface, colorFormat, decoder, outputSurface);
        } finally {
            if (VERBOSE) Log.d(TAG, "releasing codecs");
            if (inputSurface != null) {
                inputSurface.release();
            }
            if (outputSurface != null) {
                outputSurface.release();
            }
            if (encoder != null) {
                encoder.stop();
                encoder.release();
            }
            if (decoder != null) {
                decoder.stop();
                decoder.release();
            }
            Log.i(TAG, "Largest color delta: " + mLargestColorDelta);
        }
    }
    /**
     * Returns the first codec capable of encoding the specified MIME type, or null if no
     * match was found.
     */
    private static MediaCodecInfo selectCodec(String mimeType) {
        int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (!codecInfo.isEncoder()) {
                continue;
            }
            String[] types = codecInfo.getSupportedTypes();
            for (int j = 0; j < types.length; j++) {
                if (types[j].equalsIgnoreCase(mimeType)) {
                    return codecInfo;
                }
            }
        }
        return null;
    }
    /**
     * Returns a color format that is supported by the codec and by this test code.  If no
     * match is found, this throws a test failure -- the set of formats known to the test
     * should be expanded for new platforms.
     */
    private static int selectColorFormat(MediaCodecInfo codecInfo, String mimeType) {
        MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType(mimeType);
        for (int i = 0; i < capabilities.colorFormats.length; i++) {
            int colorFormat = capabilities.colorFormats[i];
            if (isRecognizedFormat(colorFormat)) {
                return colorFormat;
            }
        }
        fail("couldn't find a good color format for " + codecInfo.getName() + " / " + mimeType);
        return 0;   // not reached
    }
    /**
     * Returns true if this is a color format that this test code understands (i.e. we know how
     * to read and generate frames in this format).
     */
    private static boolean isRecognizedFormat(int colorFormat) {
        switch (colorFormat) {
            // these are the formats we know how to handle for this test
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:
                return true;
            default:
                return false;
        }
    }
    /**
     * Returns true if the specified color format is semi-planar YUV.  Throws an exception
     * if the color format is not recognized (e.g. not YUV).
     */
    private static boolean isSemiPlanarYUV(int colorFormat) {
        switch (colorFormat) {
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
                return false;
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:
                return true;
            default:
                throw new RuntimeException("unknown format " + colorFormat);
        }
    }
    /**
     * Does the actual work for encoding frames from buffers of byte[].
     */
    private void doEncodeDecodeVideoFromBuffer(MediaCodec encoder, int encoderColorFormat,
            MediaCodec decoder, boolean toSurface) {
        final int TIMEOUT_USEC = 10000;
        ByteBuffer[] encoderInputBuffers = encoder.getInputBuffers();
        ByteBuffer[] encoderOutputBuffers = encoder.getOutputBuffers();
        ByteBuffer[] decoderInputBuffers = null;
        ByteBuffer[] decoderOutputBuffers = null;
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        MediaFormat decoderOutputFormat = null;
        int generateIndex = 0;
        int checkIndex = 0;
        int badFrames = 0;
        boolean decoderConfigured = false;
        OutputSurface outputSurface = null;
        // The size of a frame of video data, in the formats we handle, is stride*sliceHeight
        // for Y, and (stride/2)*(sliceHeight/2) for each of the Cb and Cr channels.  Application
        // of algebra and assuming that stride==width and sliceHeight==height yields:
        byte[] frameData = new byte[mWidth * mHeight * 3 / 2];
        // Just out of curiosity.
        long rawSize = 0;
        long encodedSize = 0;
        // Save a copy to disk.  Useful for debugging the test.  Note this is a raw elementary
        // stream, not a .mp4 file, so not all players will know what to do with it.
        FileOutputStream outputStream = null;
        if (DEBUG_SAVE_FILE) {
            String fileName = DEBUG_FILE_NAME_BASE + mWidth + "x" + mHeight + ".mp4";
            try {
                outputStream = new FileOutputStream(fileName);
                Log.d(TAG, "encoded output will be saved as " + fileName);
            } catch (IOException ioe) {
                Log.w(TAG, "Unable to create debug output file " + fileName);
                throw new RuntimeException(ioe);
            }
        }
        if (toSurface) {
            outputSurface = new OutputSurface(mWidth, mHeight);
        }
        // Loop until the output side is done.
        boolean inputDone = false;
        boolean encoderDone = false;
        boolean outputDone = false;
        while (!outputDone) {
            if (VERBOSE) Log.d(TAG, "loop");
            // If we're not done submitting frames, generate a new one and submit it.  By
            // doing this on every loop we're working to ensure that the encoder always has
            // work to do.
            //
            // We don't really want a timeout here, but sometimes there's a delay opening
            // the encoder device, so a short timeout can keep us from spinning hard.
            if (!inputDone) {
                int inputBufIndex = encoder.dequeueInputBuffer(TIMEOUT_USEC);
                if (VERBOSE) Log.d(TAG, "inputBufIndex=" + inputBufIndex);
                if (inputBufIndex >= 0) {
                    long ptsUsec = computePresentationTime(generateIndex);
                    if (generateIndex == NUM_FRAMES) {
                        // Send an empty frame with the end-of-stream flag set.  If we set EOS
                        // on a frame with data, that frame data will be ignored, and the
                        // output will be short one frame.
                        encoder.queueInputBuffer(inputBufIndex, 0, 0, ptsUsec,
                                MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        inputDone = true;
                        if (VERBOSE) Log.d(TAG, "sent input EOS (with zero-length frame)");
                    } else {
                        generateFrame(generateIndex, encoderColorFormat, frameData);
                        ByteBuffer inputBuf = encoderInputBuffers[inputBufIndex];
                        // the buffer should be sized to hold one full frame
                        assertTrue(inputBuf.capacity() >= frameData.length);
                        inputBuf.clear();
                        inputBuf.put(frameData);
                        encoder.queueInputBuffer(inputBufIndex, 0, frameData.length, ptsUsec, 0);
                        if (VERBOSE) Log.d(TAG, "submitted frame " + generateIndex + " to enc");
                    }
                    generateIndex++;
                } else {
                    // either all in use, or we timed out during initial setup
                    if (VERBOSE) Log.d(TAG, "input buffer not available");
                }
            }
            // Check for output from the encoder.  If there's no output yet, we either need to
            // provide more input, or we need to wait for the encoder to work its magic.  We
            // can't actually tell which is the case, so if we can't get an output buffer right
            // away we loop around and see if it wants more input.
            //
            // Once we get EOS from the encoder, we don't need to do this anymore.
            if (!encoderDone) {
                int encoderStatus = encoder.dequeueOutputBuffer(info, TIMEOUT_USEC);
                if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // no output available yet
                    if (VERBOSE) Log.d(TAG, "no output from encoder available");
                } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    // not expected for an encoder
                    encoderOutputBuffers = encoder.getOutputBuffers();
                    if (VERBOSE) Log.d(TAG, "encoder output buffers changed");
                } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    // not expected for an encoder
                    MediaFormat newFormat = encoder.getOutputFormat();
                    if (VERBOSE) Log.d(TAG, "encoder output format changed: " + newFormat);
                } else if (encoderStatus < 0) {
                    fail("unexpected result from encoder.dequeueOutputBuffer: " + encoderStatus);
                } else { // encoderStatus >= 0
                    ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                    if (encodedData == null) {
                        fail("encoderOutputBuffer " + encoderStatus + " was null");
                    }
                    // It's usually necessary to adjust the ByteBuffer values to match BufferInfo.
                    encodedData.position(info.offset);
                    encodedData.limit(info.offset + info.size);
                    encodedSize += info.size;
                    if (outputStream != null) {
                        byte[] data = new byte[info.size];
                        encodedData.get(data);
                        encodedData.position(info.offset);
                        try {
                            outputStream.write(data);
                        } catch (IOException ioe) {
                            Log.w(TAG, "failed writing debug data to file");
                            throw new RuntimeException(ioe);
                        }
                    }
                    if ((info.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                        // Codec config info.  Only expected on first packet.  One way to
                        // handle this is to manually stuff the data into the MediaFormat
                        // and pass that to configure().  We do that here to exercise the API.
                        assertFalse(decoderConfigured);
                        MediaFormat format =
                                MediaFormat.createVideoFormat(MIME_TYPE, mWidth, mHeight);
                        format.setByteBuffer("csd-0", encodedData);
                        decoder.configure(format, toSurface ? outputSurface.getSurface() : null,
                                null, 0);
                        decoder.start();
                        decoderInputBuffers = decoder.getInputBuffers();
                        decoderOutputBuffers = decoder.getOutputBuffers();
                        decoderConfigured = true;
                        if (VERBOSE) Log.d(TAG, "decoder configured (" + info.size + " bytes)");
                    } else {
                        // Get a decoder input buffer, blocking until it's available.
                        assertTrue(decoderConfigured);
                        int inputBufIndex = decoder.dequeueInputBuffer(-1);
                        ByteBuffer inputBuf = decoderInputBuffers[inputBufIndex];
                        inputBuf.clear();
                        inputBuf.put(encodedData);
                        decoder.queueInputBuffer(inputBufIndex, 0, info.size,
                                info.presentationTimeUs, info.flags);
                        encoderDone = (info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0;
                        if (VERBOSE) Log.d(TAG, "passed " + info.size + " bytes to decoder"
                                + (encoderDone ? " (EOS)" : ""));
                    }
                    encoder.releaseOutputBuffer(encoderStatus, false);
                }
            }
            // Check for output from the decoder.  We want to do this on every loop to avoid
            // the possibility of stalling the pipeline.  We use a short timeout to avoid
            // burning CPU if the decoder is hard at work but the next frame isn't quite ready.
            //
            // If we're decoding to a Surface, we'll get notified here as usual but the
            // ByteBuffer references will be null.  The data is sent to Surface instead.
            if (decoderConfigured) {
                int decoderStatus = decoder.dequeueOutputBuffer(info, TIMEOUT_USEC);
                if (decoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // no output available yet
                    if (VERBOSE) Log.d(TAG, "no output from decoder available");
                } else if (decoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    // The storage associated with the direct ByteBuffer may already be unmapped,
                    // so attempting to access data through the old output buffer array could
                    // lead to a native crash.
                    if (VERBOSE) Log.d(TAG, "decoder output buffers changed");
                    decoderOutputBuffers = decoder.getOutputBuffers();
                } else if (decoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    // this happens before the first frame is returned
                    decoderOutputFormat = decoder.getOutputFormat();
                    if (VERBOSE) Log.d(TAG, "decoder output format changed: " +
                            decoderOutputFormat);
                } else if (decoderStatus < 0) {
                    fail("unexpected result from deocder.dequeueOutputBuffer: " + decoderStatus);
                } else {  // decoderStatus >= 0
                    if (!toSurface) {
                        ByteBuffer outputFrame = decoderOutputBuffers[decoderStatus];
                        outputFrame.position(info.offset);
                        outputFrame.limit(info.offset + info.size);
                        rawSize += info.size;
                        if (info.size == 0) {
                            if (VERBOSE) Log.d(TAG, "got empty frame");
                        } else {
                            if (VERBOSE) Log.d(TAG, "decoded, checking frame " + checkIndex);
                            assertEquals("Wrong time stamp", computePresentationTime(checkIndex),
                                    info.presentationTimeUs);
                            if (!checkFrame(checkIndex++, decoderOutputFormat, outputFrame)) {
                                badFrames++;
                            }
                        }
                        if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            if (VERBOSE) Log.d(TAG, "output EOS");
                            outputDone = true;
                        }
                        decoder.releaseOutputBuffer(decoderStatus, false /*render*/);
                    } else {
                        if (VERBOSE) Log.d(TAG, "surface decoder given buffer " + decoderStatus +
                                " (size=" + info.size + ")");
                        rawSize += info.size;
                        if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            if (VERBOSE) Log.d(TAG, "output EOS");
                            outputDone = true;
                        }
                        boolean doRender = (info.size != 0);
                        // As soon as we call releaseOutputBuffer, the buffer will be forwarded
                        // to SurfaceTexture to convert to a texture.  The API doesn't guarantee
                        // that the texture will be available before the call returns, so we
                        // need to wait for the onFrameAvailable callback to fire.
                        decoder.releaseOutputBuffer(decoderStatus, doRender);
                        if (doRender) {
                            if (VERBOSE) Log.d(TAG, "awaiting frame " + checkIndex);
                            assertEquals("Wrong time stamp", computePresentationTime(checkIndex),
                                    info.presentationTimeUs);
                            outputSurface.awaitNewImage();
                            outputSurface.drawImage();
                            if (!checkSurfaceFrame(checkIndex++)) {
                                badFrames++;
                            }
                        }
                    }
                }
            }
        }
        if (VERBOSE) Log.d(TAG, "decoded " + checkIndex + " frames at "
                + mWidth + "x" + mHeight + ": raw=" + rawSize + ", enc=" + encodedSize);
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException ioe) {
                Log.w(TAG, "failed closing debug file");
                throw new RuntimeException(ioe);
            }
        }
        if (outputSurface != null) {
            outputSurface.release();
        }
        if (checkIndex != NUM_FRAMES) {
            fail("expected " + NUM_FRAMES + " frames, only decoded " + checkIndex);
        }
        if (badFrames != 0) {
            fail("Found " + badFrames + " bad frames");
        }
    }
    /**
     * Does the actual work for encoding and decoding from Surface to Surface.
     */
    private void doEncodeDecodeVideoFromSurfaceToSurface(MediaCodec encoder,
            InputSurface inputSurface, int encoderColorFormat, MediaCodec decoder,
            OutputSurface outputSurface) {
        final int TIMEOUT_USEC = 10000;
        ByteBuffer[] encoderOutputBuffers = encoder.getOutputBuffers();
        ByteBuffer[] decoderInputBuffers = decoder.getInputBuffers();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int generateIndex = 0;
        int checkIndex = 0;
        int badFrames = 0;
        // Save a copy to disk.  Useful for debugging the test.  Note this is a raw elementary
        // stream, not a .mp4 file, so not all players will know what to do with it.
        FileOutputStream outputStream = null;
        if (DEBUG_SAVE_FILE) {
            String fileName = DEBUG_FILE_NAME_BASE + mWidth + "x" + mHeight + ".mp4";
            try {
                outputStream = new FileOutputStream(fileName);
                Log.d(TAG, "encoded output will be saved as " + fileName);
            } catch (IOException ioe) {
                Log.w(TAG, "Unable to create debug output file " + fileName);
                throw new RuntimeException(ioe);
            }
        }
        // Loop until the output side is done.
        boolean inputDone = false;
        boolean encoderDone = false;
        boolean outputDone = false;
        while (!outputDone) {
            if (VERBOSE) Log.d(TAG, "loop");
            // If we're not done submitting frames, generate a new one and submit it.  The
            // eglSwapBuffers call will block if the input is full.
            if (!inputDone) {
                if (generateIndex == NUM_FRAMES) {
                    // Send an empty frame with the end-of-stream flag set.
                    if (VERBOSE) Log.d(TAG, "signaling input EOS");
                    encoder.signalEndOfInputStream();
                    inputDone = true;
                } else {
                    inputSurface.makeCurrent();
                    generateSurfaceFrame(generateIndex);
                    inputSurface.setPresentationTime(computePresentationTime(generateIndex) * 1000);
                    if (VERBOSE) Log.d(TAG, "inputSurface swapBuffers");
                    inputSurface.swapBuffers();
                }
                generateIndex++;
            }
            // Assume output is available.  Loop until both assumptions are false.
            boolean decoderOutputAvailable = true;
            boolean encoderOutputAvailable = !encoderDone;
            while (decoderOutputAvailable || encoderOutputAvailable) {
                // Start by draining any pending output from the decoder.  It's important to
                // do this before we try to stuff any more data in.
                int decoderStatus = decoder.dequeueOutputBuffer(info, TIMEOUT_USEC);
                if (decoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // no output available yet
                    if (VERBOSE) Log.d(TAG, "no output from decoder available");
                    decoderOutputAvailable = false;
                } else if (decoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    if (VERBOSE) Log.d(TAG, "decoder output buffers changed (but we don't care)");
                } else if (decoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    // this happens before the first frame is returned
                    MediaFormat decoderOutputFormat = decoder.getOutputFormat();
                    if (VERBOSE) Log.d(TAG, "decoder output format changed: " +
                            decoderOutputFormat);
                } else if (decoderStatus < 0) {
                    fail("unexpected result from deocder.dequeueOutputBuffer: " + decoderStatus);
                } else {  // decoderStatus >= 0
                    if (VERBOSE) Log.d(TAG, "surface decoder given buffer " + decoderStatus +
                            " (size=" + info.size + ")");
                    if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        if (VERBOSE) Log.d(TAG, "output EOS");
                        outputDone = true;
                    }
                    // The ByteBuffers are null references, but we still get a nonzero size for
                    // the decoded data.
                    boolean doRender = (info.size != 0);
                    // As soon as we call releaseOutputBuffer, the buffer will be forwarded
                    // to SurfaceTexture to convert to a texture.  The API doesn't guarantee
                    // that the texture will be available before the call returns, so we
                    // need to wait for the onFrameAvailable callback to fire.  If we don't
                    // wait, we risk dropping frames.
                    outputSurface.makeCurrent();
                    decoder.releaseOutputBuffer(decoderStatus, doRender);
                    if (doRender) {
                        assertEquals("Wrong time stamp", computePresentationTime(checkIndex),
                                info.presentationTimeUs);
                        if (VERBOSE) Log.d(TAG, "awaiting frame " + checkIndex);
                        outputSurface.awaitNewImage();
                        outputSurface.drawImage();
                        if (!checkSurfaceFrame(checkIndex++)) {
                            badFrames++;
                        }
                    }
                }
                if (decoderStatus != MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // Continue attempts to drain output.
                    continue;
                }
                // Decoder is drained, check to see if we've got a new buffer of output from
                // the encoder.
                if (!encoderDone) {
                    int encoderStatus = encoder.dequeueOutputBuffer(info, TIMEOUT_USEC);
                    if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                        // no output available yet
                        if (VERBOSE) Log.d(TAG, "no output from encoder available");
                        encoderOutputAvailable = false;
                    } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                        // not expected for an encoder
                        encoderOutputBuffers = encoder.getOutputBuffers();
                        if (VERBOSE) Log.d(TAG, "encoder output buffers changed");
                    } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                        // not expected for an encoder
                        MediaFormat newFormat = encoder.getOutputFormat();
                        if (VERBOSE) Log.d(TAG, "encoder output format changed: " + newFormat);
                    } else if (encoderStatus < 0) {
                        fail("unexpected result from encoder.dequeueOutputBuffer: " + encoderStatus);
                    } else { // encoderStatus >= 0
                        ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                        if (encodedData == null) {
                            fail("encoderOutputBuffer " + encoderStatus + " was null");
                        }
                        // It's usually necessary to adjust the ByteBuffer values to match BufferInfo.
                        encodedData.position(info.offset);
                        encodedData.limit(info.offset + info.size);
                        if (outputStream != null) {
                            byte[] data = new byte[info.size];
                            encodedData.get(data);
                            encodedData.position(info.offset);
                            try {
                                outputStream.write(data);
                            } catch (IOException ioe) {
                                Log.w(TAG, "failed writing debug data to file");
                                throw new RuntimeException(ioe);
                            }
                        }
                        // Get a decoder input buffer, blocking until it's available.  We just
                        // drained the decoder output, so we expect there to be a free input
                        // buffer now or in the near future (i.e. this should never deadlock
                        // if the codec is meeting requirements).
                        //
                        // The first buffer of data we get will have the BUFFER_FLAG_CODEC_CONFIG
                        // flag set; the decoder will see this and finish configuring itself.
                        int inputBufIndex = decoder.dequeueInputBuffer(-1);
                        ByteBuffer inputBuf = decoderInputBuffers[inputBufIndex];
                        inputBuf.clear();
                        inputBuf.put(encodedData);
                        decoder.queueInputBuffer(inputBufIndex, 0, info.size,
                                info.presentationTimeUs, info.flags);
                        // If everything from the encoder has been passed to the decoder, we
                        // can stop polling the encoder output.  (This just an optimization.)
                        if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            encoderDone = true;
                            encoderOutputAvailable = false;
                        }
                        if (VERBOSE) Log.d(TAG, "passed " + info.size + " bytes to decoder"
                                + (encoderDone ? " (EOS)" : ""));
                        encoder.releaseOutputBuffer(encoderStatus, false);
                    }
                }
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException ioe) {
                Log.w(TAG, "failed closing debug file");
                throw new RuntimeException(ioe);
            }
        }
        if (checkIndex != NUM_FRAMES) {
            fail("expected " + NUM_FRAMES + " frames, only decoded " + checkIndex);
        }
        if (badFrames != 0) {
            fail("Found " + badFrames + " bad frames");
        }
    }
    /**
     * Generates data for frame N into the supplied buffer.  We have an 8-frame animation
     * sequence that wraps around.  It looks like this:
     * <pre>
     *   0 1 2 3
     *   7 6 5 4
     * </pre>
     * We draw one of the eight rectangles and leave the rest set to the zero-fill color.
     */
    private void generateFrame(int frameIndex, int colorFormat, byte[] frameData) {
        final int HALF_WIDTH = mWidth / 2;
        boolean semiPlanar = isSemiPlanarYUV(colorFormat);
        // Set to zero.  In YUV this is a dull green.
        Arrays.fill(frameData, (byte) 0);
        int startX, startY, countX, countY;
        frameIndex %= 8;
        //frameIndex = (frameIndex / 8) % 8;    // use this instead for debug -- easier to see
        if (frameIndex < 4) {
            startX = frameIndex * (mWidth / 4);
            startY = 0;
        } else {
            startX = (7 - frameIndex) * (mWidth / 4);
            startY = mHeight / 2;
        }
        for (int y = startY + (mHeight/2) - 1; y >= startY; --y) {
            for (int x = startX + (mWidth/4) - 1; x >= startX; --x) {
                if (semiPlanar) {
                    // full-size Y, followed by UV pairs at half resolution
                    // e.g. Nexus 4 OMX.qcom.video.encoder.avc COLOR_FormatYUV420SemiPlanar
                    // e.g. Galaxy Nexus OMX.TI.DUCATI1.VIDEO.H264E
                    //        OMX_TI_COLOR_FormatYUV420PackedSemiPlanar
                    frameData[y * mWidth + x] = (byte) TEST_Y;
                    if ((x & 0x01) == 0 && (y & 0x01) == 0) {
                        frameData[mWidth*mHeight + y * HALF_WIDTH + x] = (byte) TEST_U;
                        frameData[mWidth*mHeight + y * HALF_WIDTH + x + 1] = (byte) TEST_V;
                    }
                } else {
                    // full-size Y, followed by quarter-size U and quarter-size V
                    // e.g. Nexus 10 OMX.Exynos.AVC.Encoder COLOR_FormatYUV420Planar
                    // e.g. Nexus 7 OMX.Nvidia.h264.encoder COLOR_FormatYUV420Planar
                    frameData[y * mWidth + x] = (byte) TEST_Y;
                    if ((x & 0x01) == 0 && (y & 0x01) == 0) {
                        frameData[mWidth*mHeight + (y/2) * HALF_WIDTH + (x/2)] = (byte) TEST_U;
                        frameData[mWidth*mHeight + HALF_WIDTH * (mHeight / 2) +
                                  (y/2) * HALF_WIDTH + (x/2)] = (byte) TEST_V;
                    }
                }
            }
        }
    }
    /**
     * Performs a simple check to see if the frame is more or less right.
     * <p>
     * See {@link #generateFrame} for a description of the layout.  The idea is to sample
     * one pixel from the middle of the 8 regions, and verify that the correct one has
     * the non-background color.  We can't know exactly what the video encoder has done
     * with our frames, so we just check to see if it looks like more or less the right thing.
     *
     * @return true if the frame looks good
     */
    private boolean checkFrame(int frameIndex, MediaFormat format, ByteBuffer frameData) {
        // Check for color formats we don't understand.  There is no requirement for video
        // decoders to use a "mundane" format, so we just give a pass on proprietary formats.
        // e.g. Nexus 4 0x7FA30C03 OMX_QCOM_COLOR_FormatYUV420PackedSemiPlanar64x32Tile2m8ka
        int colorFormat = format.getInteger(MediaFormat.KEY_COLOR_FORMAT);
        if (!isRecognizedFormat(colorFormat)) {
            Log.d(TAG, "unable to check frame contents for colorFormat=" +
                    Integer.toHexString(colorFormat));
            return true;
        }
        boolean frameFailed = false;
        boolean semiPlanar = isSemiPlanarYUV(colorFormat);
        int width = format.getInteger(MediaFormat.KEY_WIDTH);
        int height = format.getInteger(MediaFormat.KEY_HEIGHT);
        int halfWidth = width / 2;
        int cropLeft = format.getInteger("crop-left");
        int cropRight = format.getInteger("crop-right");
        int cropTop = format.getInteger("crop-top");
        int cropBottom = format.getInteger("crop-bottom");
        int cropWidth = cropRight - cropLeft + 1;
        int cropHeight = cropBottom - cropTop + 1;
        assertEquals(mWidth, cropWidth);
        assertEquals(mHeight, cropHeight);
        for (int i = 0; i < 8; i++) {
            int x, y;
            if (i < 4) {
                x = i * (mWidth / 4) + (mWidth / 8);
                y = mHeight / 4;
            } else {
                x = (7 - i) * (mWidth / 4) + (mWidth / 8);
                y = (mHeight * 3) / 4;
            }
            y += cropTop;
            x += cropLeft;
            int testY, testU, testV;
            if (semiPlanar) {
                // Galaxy Nexus uses OMX_TI_COLOR_FormatYUV420PackedSemiPlanar
                testY = frameData.get(y * width + x) & 0xff;
                testU = frameData.get(width*height + 2*(y/2) * halfWidth + 2*(x/2)) & 0xff;
                testV = frameData.get(width*height + 2*(y/2) * halfWidth + 2*(x/2) + 1) & 0xff;
            } else {
                // Nexus 10, Nexus 7 use COLOR_FormatYUV420Planar
                testY = frameData.get(y * width + x) & 0xff;
                testU = frameData.get(width*height + (y/2) * halfWidth + (x/2)) & 0xff;
                testV = frameData.get(width*height + halfWidth * (height / 2) +
                        (y/2) * halfWidth + (x/2)) & 0xff;
            }
            int expY, expU, expV;
            if (i == frameIndex % 8) {
                // colored rect
                expY = TEST_Y;
                expU = TEST_U;
                expV = TEST_V;
            } else {
                // should be our zeroed-out buffer
                expY = expU = expV = 0;
            }
            if (!isColorClose(testY, expY) ||
                    !isColorClose(testU, expU) ||
                    !isColorClose(testV, expV)) {
                Log.w(TAG, "Bad frame " + frameIndex + " (rect=" + i + ": yuv=" + testY +
                        "," + testU + "," + testV + " vs. expected " + expY + "," + expU +
                        "," + expV + ")");
                frameFailed = true;
            }
        }
        return !frameFailed;
    }
    /**
     * Generates a frame of data using GL commands.
     */
    private void generateSurfaceFrame(int frameIndex) {
        frameIndex %= 8;
        int startX, startY;
        if (frameIndex < 4) {
            // (0,0) is bottom-left in GL
            startX = frameIndex * (mWidth / 4);
            startY = mHeight / 2;
        } else {
            startX = (7 - frameIndex) * (mWidth / 4);
            startY = 0;
        }
        GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
        GLES20.glClearColor(TEST_R0 / 255.0f, TEST_G0 / 255.0f, TEST_B0 / 255.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
        GLES20.glScissor(startX, startY, mWidth / 4, mHeight / 2);
        GLES20.glClearColor(TEST_R1 / 255.0f, TEST_G1 / 255.0f, TEST_B1 / 255.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }
    /**
     * Checks the frame for correctness.  Similar to {@link #checkFrame}, but uses GL to
     * read pixels from the current surface.
     *
     * @return true if the frame looks good
     */
    private boolean checkSurfaceFrame(int frameIndex) {
        ByteBuffer pixelBuf = ByteBuffer.allocateDirect(4); // TODO - reuse this
        boolean frameFailed = false;
        for (int i = 0; i < 8; i++) {
            // Note the coordinates are inverted on the Y-axis in GL.
            int x, y;
            if (i < 4) {
                x = i * (mWidth / 4) + (mWidth / 8);
                y = (mHeight * 3) / 4;
            } else {
                x = (7 - i) * (mWidth / 4) + (mWidth / 8);
                y = mHeight / 4;
            }
            GLES20.glReadPixels(x, y, 1, 1, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, pixelBuf);
            int r = pixelBuf.get(0) & 0xff;
            int g = pixelBuf.get(1) & 0xff;
            int b = pixelBuf.get(2) & 0xff;
            //Log.d(TAG, "GOT(" + frameIndex + "/" + i + "): r=" + r + " g=" + g + " b=" + b);
            int expR, expG, expB;
            if (i == frameIndex % 8) {
                // colored rect
                expR = TEST_R1;
                expG = TEST_G1;
                expB = TEST_B1;
            } else {
                // zero background color
                expR = TEST_R0;
                expG = TEST_G0;
                expB = TEST_B0;
            }
            if (!isColorClose(r, expR) ||
                    !isColorClose(g, expG) ||
                    !isColorClose(b, expB)) {
                Log.w(TAG, "Bad frame " + frameIndex + " (rect=" + i + ": rgb=" + r +
                        "," + g + "," + b + " vs. expected " + expR + "," + expG +
                        "," + expB + ")");
                frameFailed = true;
            }
        }
        return !frameFailed;
    }
    /**
     * Returns true if the actual color value is close to the expected color value.  Updates
     * mLargestColorDelta.
     */
    boolean isColorClose(int actual, int expected) {
        final int MAX_DELTA = 8;
        int delta = Math.abs(actual - expected);
        if (delta > mLargestColorDelta) {
            mLargestColorDelta = delta;
        }
        return (delta <= MAX_DELTA);
    }
    /**
     * Generates the presentation time for frame N, in microseconds.
     */
    private static long computePresentationTime(int frameIndex) {
        return 132 + frameIndex * 1000000 / FRAME_RATE;
    }
}
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;
import android.view.Surface;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//20131106: removed unnecessary glFinish(), removed hard-coded "/sdcard"
//20131205: added alpha to EGLConfig
//20131210: demonstrate un-bind and re-bind of texture, for apps with shared EGL contexts
//20140123: correct error checks on glGet*Location() and program creation (they don't set error)

/**
 * Record video from the camera preview and encode it as an MP4 file.  Demonstrates the use
 * of MediaMuxer and MediaCodec with Camera input.  Does not record audio.
 * <p>
 * Generally speaking, it's better to use MediaRecorder for this sort of thing.  This example
 * demonstrates one possible advantage: editing of video as it's being encoded.  A GLES 2.0
 * fragment shader is used to perform a silly color tweak every 15 frames.
 * <p>
 * This uses various features first available in Android "Jellybean" 4.3 (API 18).  There is
 * no equivalent functionality in previous releases.  (You can send the Camera preview to a
 * byte buffer with a fully-specified format, but MediaCodec encoders want different input
 * formats on different devices, and this use case wasn't well exercised in CTS pre-4.3.)
 * <p>
 * The output file will be something like "/sdcard/test.640x480.mp4".
 * <p>
 * (This was derived from bits and pieces of CTS tests, and is packaged as such, but is not
 * currently part of CTS.)
 */
public class CameraToMpegTest extends AndroidTestCase {
    private static final String TAG = "CameraToMpegTest";
    private static final boolean VERBOSE = false;           // lots of logging

    // where to put the output file (note: /sdcard requires WRITE_EXTERNAL_STORAGE permission)
    private static final File OUTPUT_DIR = Environment.getExternalStorageDirectory();

    // parameters for the encoder
    private static final String MIME_TYPE = "video/avc";    // H.264 Advanced Video Coding
    private static final int FRAME_RATE = 30;               // 30fps
    private static final int IFRAME_INTERVAL = 5;           // 5 seconds between I-frames
    private static final long DURATION_SEC = 8;             // 8 seconds of video

    // Fragment shader that swaps color channels around.
    private static final String SWAPPED_FRAGMENT_SHADER =
            "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            "varying vec2 vTextureCoord;\n" +
            "uniform samplerExternalOES sTexture;\n" +
            "void main() {\n" +
            "  gl_FragColor = texture2D(sTexture, vTextureCoord).gbra;\n" +
            "}\n";

    // encoder / muxer state
    private MediaCodec mEncoder;
    private CodecInputSurface mInputSurface;
    private MediaMuxer mMuxer;
    private int mTrackIndex;
    private boolean mMuxerStarted;

    // camera state
    private Camera mCamera;
    private SurfaceTextureManager mStManager;

    // allocate one of these up front so we don't need to do it every time
    private MediaCodec.BufferInfo mBufferInfo;

    /** test entry point */
    public void testEncodeCameraToMp4() throws Throwable {
        CameraToMpegWrapper.runTest(this);
    }

    /**
     * Wraps encodeCameraToMpeg().  This is necessary because SurfaceTexture will try to use
     * the looper in the current thread if one exists, and the CTS tests create one on the
     * test thread.
     *
     * The wrapper propagates exceptions thrown by the worker thread back to the caller.
     */
    private static class CameraToMpegWrapper implements Runnable {
        private Throwable mThrowable;
        private CameraToMpegTest mTest;

        private CameraToMpegWrapper(CameraToMpegTest test) {
            mTest = test;
        }

        @Override
        public void run() {
            try {
                mTest.encodeCameraToMpeg();
            } catch (Throwable th) {
                mThrowable = th;
            }
        }

        /** Entry point. */
        public static void runTest(CameraToMpegTest obj) throws Throwable {
            CameraToMpegWrapper wrapper = new CameraToMpegWrapper(obj);
            Thread th = new Thread(wrapper, "codec test");
            th.start();
            th.join();
            if (wrapper.mThrowable != null) {
                throw wrapper.mThrowable;
            }
        }
    }

    /**
     * Tests encoding of AVC video from Camera input.  The output is saved as an MP4 file.
     */
    private void encodeCameraToMpeg() {
        // arbitrary but popular values
        int encWidth = 640;
        int encHeight = 480;
        int encBitRate = 6000000;      // Mbps
        Log.d(TAG, MIME_TYPE + " output " + encWidth + "x" + encHeight + " @" + encBitRate);

        try {
            prepareCamera(encWidth, encHeight);
            prepareEncoder(encWidth, encHeight, encBitRate);
            mInputSurface.makeCurrent();
            prepareSurfaceTexture();

            mCamera.startPreview();

            long startWhen = System.nanoTime();
            long desiredEnd = startWhen + DURATION_SEC * 1000000000L;
            SurfaceTexture st = mStManager.getSurfaceTexture();
            int frameCount = 0;

            while (System.nanoTime() < desiredEnd) {
                // Feed any pending encoder output into the muxer.
                drainEncoder(false);

                // Switch up the colors every 15 frames.  Besides demonstrating the use of
                // fragment shaders for video editing, this provides a visual indication of
                // the frame rate: if the camera is capturing at 15fps, the colors will change
                // once per second.
                if ((frameCount % 15) == 0) {
                    String fragmentShader = null;
                    if ((frameCount & 0x01) != 0) {
                        fragmentShader = SWAPPED_FRAGMENT_SHADER;
                    }
                    mStManager.changeFragmentShader(fragmentShader);
                }
                frameCount++;

                // Acquire a new frame of input, and render it to the Surface.  If we had a
                // GLSurfaceView we could switch EGL contexts and call drawImage() a second
                // time to render it on screen.  The texture can be shared between contexts by
                // passing the GLSurfaceView's EGLContext as eglCreateContext()'s share_context
                // argument.
                mStManager.awaitNewImage();
                mStManager.drawImage();

                // Set the presentation time stamp from the SurfaceTexture's time stamp.  This
                // will be used by MediaMuxer to set the PTS in the video.
                if (VERBOSE) {
                    Log.d(TAG, "present: " +
                            ((st.getTimestamp() - startWhen) / 1000000.0) + "ms");
                }
                mInputSurface.setPresentationTime(st.getTimestamp());

                // Submit it to the encoder.  The eglSwapBuffers call will block if the input
                // is full, which would be bad if it stayed full until we dequeued an output
                // buffer (which we can't do, since we're stuck here).  So long as we fully drain
                // the encoder before supplying additional input, the system guarantees that we
                // can supply another frame without blocking.
                if (VERBOSE) Log.d(TAG, "sending frame to encoder");
                mInputSurface.swapBuffers();
            }

            // send end-of-stream to encoder, and drain remaining output
            drainEncoder(true);
        } finally {
            // release everything we grabbed
            releaseCamera();
            releaseEncoder();
            releaseSurfaceTexture();
        }
    }

    /**
     * Configures Camera for video capture.  Sets mCamera.
     * <p>
     * Opens a Camera and sets parameters.  Does not start preview.
     */
    private void prepareCamera(int encWidth, int encHeight) {
        if (mCamera != null) {
            throw new RuntimeException("camera already initialized");
        }

        Camera.CameraInfo info = new Camera.CameraInfo();

        // Try to find a front-facing camera (e.g. for videoconferencing).
        int numCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numCameras; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mCamera = Camera.open(i);
                break;
            }
        }
        if (mCamera == null) {
            Log.d(TAG, "No front-facing camera found; opening default");
            mCamera = Camera.open();    // opens first back-facing camera
        }
        if (mCamera == null) {
            throw new RuntimeException("Unable to open camera");
        }

        Camera.Parameters parms = mCamera.getParameters();

        choosePreviewSize(parms, encWidth, encHeight);
        // leave the frame rate set to default
        mCamera.setParameters(parms);

        Camera.Size size = parms.getPreviewSize();
        Log.d(TAG, "Camera preview size is " + size.width + "x" + size.height);
    }

    /**
     * Attempts to find a preview size that matches the provided width and height (which
     * specify the dimensions of the encoded video).  If it fails to find a match it just
     * uses the default preview size.
     * <p>
     * TODO: should do a best-fit match.
     */
    private static void choosePreviewSize(Camera.Parameters parms, int width, int height) {
        // We should make sure that the requested MPEG size is less than the preferred
        // size, and has the same aspect ratio.
        Camera.Size ppsfv = parms.getPreferredPreviewSizeForVideo();
        if (VERBOSE && ppsfv != null) {
            Log.d(TAG, "Camera preferred preview size for video is " +
                    ppsfv.width + "x" + ppsfv.height);
        }

        for (Camera.Size size : parms.getSupportedPreviewSizes()) {
            if (size.width == width && size.height == height) {
                parms.setPreviewSize(width, height);
                return;
            }
        }

        Log.w(TAG, "Unable to set preview size to " + width + "x" + height);
        if (ppsfv != null) {
            parms.setPreviewSize(ppsfv.width, ppsfv.height);
        }
    }

    /**
     * Stops camera preview, and releases the camera to the system.
     */
    private void releaseCamera() {
        if (VERBOSE) Log.d(TAG, "releasing camera");
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * Configures SurfaceTexture for camera preview.  Initializes mStManager, and sets the
     * associated SurfaceTexture as the Camera's "preview texture".
     * <p>
     * Configure the EGL surface that will be used for output before calling here.
     */
    private void prepareSurfaceTexture() {
        mStManager = new SurfaceTextureManager();
        SurfaceTexture st = mStManager.getSurfaceTexture();
        try {
            mCamera.setPreviewTexture(st);
        } catch (IOException ioe) {
            throw new RuntimeException("setPreviewTexture failed", ioe);
        }
    }

    /**
     * Releases the SurfaceTexture.
     */
    private void releaseSurfaceTexture() {
        if (mStManager != null) {
            mStManager.release();
            mStManager = null;
        }
    }

    /**
     * Configures encoder and muxer state, and prepares the input Surface.  Initializes
     * mEncoder, mMuxer, mInputSurface, mBufferInfo, mTrackIndex, and mMuxerStarted.
     */
    private void prepareEncoder(int width, int height, int bitRate) {
        mBufferInfo = new MediaCodec.BufferInfo();

        MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, width, height);

        // Set some properties.  Failing to specify some of these can cause the MediaCodec
        // configure() call to throw an unhelpful exception.
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);
        if (VERBOSE) Log.d(TAG, "format: " + format);

        // Create a MediaCodec encoder, and configure it with our format.  Get a Surface
        // we can use for input and wrap it with a class that handles the EGL work.
        //
        // If you want to have two EGL contexts -- one for display, one for recording --
        // you will likely want to defer instantiation of CodecInputSurface until after the
        // "display" EGL context is created, then modify the eglCreateContext call to
        // take eglGetCurrentContext() as the share_context argument.
        mEncoder = MediaCodec.createEncoderByType(MIME_TYPE);
        mEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mInputSurface = new CodecInputSurface(mEncoder.createInputSurface());
        mEncoder.start();

        // Output filename.  Ideally this would use Context.getFilesDir() rather than a
        // hard-coded output directory.
        String outputPath = new File(OUTPUT_DIR,
                "test." + width + "x" + height + ".mp4").toString();
        Log.i(TAG, "Output file is " + outputPath);


        // Create a MediaMuxer.  We can't add the video track and start() the muxer here,
        // because our MediaFormat doesn't have the Magic Goodies.  These can only be
        // obtained from the encoder after it has started processing data.
        //
        // We're not actually interested in multiplexing audio.  We just want to convert
        // the raw H.264 elementary stream we get from MediaCodec into a .mp4 file.
        try {
            mMuxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException ioe) {
            throw new RuntimeException("MediaMuxer creation failed", ioe);
        }

        mTrackIndex = -1;
        mMuxerStarted = false;
    }

    /**
     * Releases encoder resources.
     */
    private void releaseEncoder() {
        if (VERBOSE) Log.d(TAG, "releasing encoder objects");
        if (mEncoder != null) {
            mEncoder.stop();
            mEncoder.release();
            mEncoder = null;
        }
        if (mInputSurface != null) {
            mInputSurface.release();
            mInputSurface = null;
        }
        if (mMuxer != null) {
            mMuxer.stop();
            mMuxer.release();
            mMuxer = null;
        }
    }

    /**
     * Extracts all pending data from the encoder and forwards it to the muxer.
     * <p>
     * If endOfStream is not set, this returns when there is no more data to drain.  If it
     * is set, we send EOS to the encoder, and then iterate until we see EOS on the output.
     * Calling this with endOfStream set should be done once, right before stopping the muxer.
     * <p>
     * We're just using the muxer to get a .mp4 file (instead of a raw H.264 stream).  We're
     * not recording audio.
     */
    private void drainEncoder(boolean endOfStream) {
        final int TIMEOUT_USEC = 10000;
        if (VERBOSE) Log.d(TAG, "drainEncoder(" + endOfStream + ")");

        if (endOfStream) {
            if (VERBOSE) Log.d(TAG, "sending EOS to encoder");
            mEncoder.signalEndOfInputStream();
        }

        ByteBuffer[] encoderOutputBuffers = mEncoder.getOutputBuffers();
        while (true) {
            int encoderStatus = mEncoder.dequeueOutputBuffer(mBufferInfo, TIMEOUT_USEC);
            if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                // no output available yet
                if (!endOfStream) {
                    break;      // out of while
                } else {
                    if (VERBOSE) Log.d(TAG, "no output available, spinning to await EOS");
                }
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                // not expected for an encoder
                encoderOutputBuffers = mEncoder.getOutputBuffers();
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // should happen before receiving buffers, and should only happen once
                if (mMuxerStarted) {
                    throw new RuntimeException("format changed twice");
                }
                MediaFormat newFormat = mEncoder.getOutputFormat();
                Log.d(TAG, "encoder output format changed: " + newFormat);

                // now that we have the Magic Goodies, start the muxer
                mTrackIndex = mMuxer.addTrack(newFormat);
                mMuxer.start();
                mMuxerStarted = true;
            } else if (encoderStatus < 0) {
                Log.w(TAG, "unexpected result from encoder.dequeueOutputBuffer: " +
                        encoderStatus);
                // let's ignore it
            } else {
                ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                if (encodedData == null) {
                    throw new RuntimeException("encoderOutputBuffer " + encoderStatus +
                            " was null");
                }

                if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    // The codec config data was pulled out and fed to the muxer when we got
                    // the INFO_OUTPUT_FORMAT_CHANGED status.  Ignore it.
                    if (VERBOSE) Log.d(TAG, "ignoring BUFFER_FLAG_CODEC_CONFIG");
                    mBufferInfo.size = 0;
                }

                if (mBufferInfo.size != 0) {
                    if (!mMuxerStarted) {
                        throw new RuntimeException("muxer hasn't started");
                    }

                    // adjust the ByteBuffer values to match BufferInfo (not needed?)
                    encodedData.position(mBufferInfo.offset);
                    encodedData.limit(mBufferInfo.offset + mBufferInfo.size);

                    mMuxer.writeSampleData(mTrackIndex, encodedData, mBufferInfo);
                    if (VERBOSE) Log.d(TAG, "sent " + mBufferInfo.size + " bytes to muxer");
                }

                mEncoder.releaseOutputBuffer(encoderStatus, false);

                if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    if (!endOfStream) {
                        Log.w(TAG, "reached end of stream unexpectedly");
                    } else {
                        if (VERBOSE) Log.d(TAG, "end of stream reached");
                    }
                    break;      // out of while
                }
            }
        }
    }


    /**
     * Holds state associated with a Surface used for MediaCodec encoder input.
     * <p>
     * The constructor takes a Surface obtained from MediaCodec.createInputSurface(), and uses
     * that to create an EGL window surface.  Calls to eglSwapBuffers() cause a frame of data to
     * be sent to the video encoder.
     * <p>
     * This object owns the Surface -- releasing this will release the Surface too.
     */
    private static class CodecInputSurface {
        private static final int EGL_RECORDABLE_ANDROID = 0x3142;

        private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;
        private EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;

        private Surface mSurface;

        /**
         * Creates a CodecInputSurface from a Surface.
         */
        public CodecInputSurface(Surface surface) {
            if (surface == null) {
                throw new NullPointerException();
            }
            mSurface = surface;

            eglSetup();
        }

        /**
         * Prepares EGL.  We want a GLES 2.0 context and a surface that supports recording.
         */
        private void eglSetup() {
            mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
            if (mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
                throw new RuntimeException("unable to get EGL14 display");
            }
            int[] version = new int[2];
            if (!EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1)) {
                throw new RuntimeException("unable to initialize EGL14");
            }

            // Configure EGL for recording and OpenGL ES 2.0.
            int[] attribList = {
                    EGL14.EGL_RED_SIZE, 8,
                    EGL14.EGL_GREEN_SIZE, 8,
                    EGL14.EGL_BLUE_SIZE, 8,
                    EGL14.EGL_ALPHA_SIZE, 8,
                    EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                    EGL_RECORDABLE_ANDROID, 1,
                    EGL14.EGL_NONE
            };
            EGLConfig[] configs = new EGLConfig[1];
            int[] numConfigs = new int[1];
            EGL14.eglChooseConfig(mEGLDisplay, attribList, 0, configs, 0, configs.length,
                    numConfigs, 0);
            checkEglError("eglCreateContext RGB888+recordable ES2");

            // Configure context for OpenGL ES 2.0.
            int[] attrib_list = {
                    EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                    EGL14.EGL_NONE
            };
            mEGLContext = EGL14.eglCreateContext(mEGLDisplay, configs[0], EGL14.EGL_NO_CONTEXT,
                    attrib_list, 0);
            checkEglError("eglCreateContext");

            // Create a window surface, and attach it to the Surface we received.
            int[] surfaceAttribs = {
                    EGL14.EGL_NONE
            };
            mEGLSurface = EGL14.eglCreateWindowSurface(mEGLDisplay, configs[0], mSurface,
                    surfaceAttribs, 0);
            checkEglError("eglCreateWindowSurface");
        }

        /**
         * Discards all resources held by this class, notably the EGL context.  Also releases the
         * Surface that was passed to our constructor.
         */
        public void release() {
            if (mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
                EGL14.eglMakeCurrent(mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE,
                        EGL14.EGL_NO_CONTEXT);
                EGL14.eglDestroySurface(mEGLDisplay, mEGLSurface);
                EGL14.eglDestroyContext(mEGLDisplay, mEGLContext);
                EGL14.eglReleaseThread();
                EGL14.eglTerminate(mEGLDisplay);
            }
            mSurface.release();

            mEGLDisplay = EGL14.EGL_NO_DISPLAY;
            mEGLContext = EGL14.EGL_NO_CONTEXT;
            mEGLSurface = EGL14.EGL_NO_SURFACE;

            mSurface = null;
        }

        /**
         * Makes our EGL context and surface current.
         */
        public void makeCurrent() {
            EGL14.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext);
            checkEglError("eglMakeCurrent");
        }

        /**
         * Calls eglSwapBuffers.  Use this to "publish" the current frame.
         */
        public boolean swapBuffers() {
            boolean result = EGL14.eglSwapBuffers(mEGLDisplay, mEGLSurface);
            checkEglError("eglSwapBuffers");
            return result;
        }

        /**
         * Sends the presentation time stamp to EGL.  Time is expressed in nanoseconds.
         */
        public void setPresentationTime(long nsecs) {
            EGLExt.eglPresentationTimeANDROID(mEGLDisplay, mEGLSurface, nsecs);
            checkEglError("eglPresentationTimeANDROID");
        }

        /**
         * Checks for EGL errors.  Throws an exception if one is found.
         */
        private void checkEglError(String msg) {
            int error;
            if ((error = EGL14.eglGetError()) != EGL14.EGL_SUCCESS) {
                throw new RuntimeException(msg + ": EGL error: 0x" + Integer.toHexString(error));
            }
        }
    }


    /**
     * Manages a SurfaceTexture.  Creates SurfaceTexture and TextureRender objects, and provides
     * functions that wait for frames and render them to the current EGL surface.
     * <p>
     * The SurfaceTexture can be passed to Camera.setPreviewTexture() to receive camera output.
     */
    private static class SurfaceTextureManager
            implements SurfaceTexture.OnFrameAvailableListener {
        private SurfaceTexture mSurfaceTexture;
        private CameraToMpegTest.STextureRender mTextureRender;

        private Object mFrameSyncObject = new Object();     // guards mFrameAvailable
        private boolean mFrameAvailable;

        /**
         * Creates instances of TextureRender and SurfaceTexture.
         */
        public SurfaceTextureManager() {
            mTextureRender = new CameraToMpegTest.STextureRender();
            mTextureRender.surfaceCreated();

            if (VERBOSE) Log.d(TAG, "textureID=" + mTextureRender.getTextureId());
            mSurfaceTexture = new SurfaceTexture(mTextureRender.getTextureId());

            // This doesn't work if this object is created on the thread that CTS started for
            // these test cases.
            //
            // The CTS-created thread has a Looper, and the SurfaceTexture constructor will
            // create a Handler that uses it.  The "frame available" message is delivered
            // there, but since we're not a Looper-based thread we'll never see it.  For
            // this to do anything useful, OutputSurface must be created on a thread without
            // a Looper, so that SurfaceTexture uses the main application Looper instead.
            //
            // Java language note: passing "this" out of a constructor is generally unwise,
            // but we should be able to get away with it here.
            mSurfaceTexture.setOnFrameAvailableListener(this);
        }

        public void release() {
            // this causes a bunch of warnings that appear harmless but might confuse someone:
            //  W BufferQueue: [unnamed-3997-2] cancelBuffer: BufferQueue has been abandoned!
            //mSurfaceTexture.release();

            mTextureRender = null;
            mSurfaceTexture = null;
        }

        /**
         * Returns the SurfaceTexture.
         */
        public SurfaceTexture getSurfaceTexture() {
            return mSurfaceTexture;
        }

        /**
         * Replaces the fragment shader.
         */
        public void changeFragmentShader(String fragmentShader) {
            mTextureRender.changeFragmentShader(fragmentShader);
        }

        /**
         * Latches the next buffer into the texture.  Must be called from the thread that created
         * the OutputSurface object.
         */
        public void awaitNewImage() {
            final int TIMEOUT_MS = 2500;

            synchronized (mFrameSyncObject) {
                while (!mFrameAvailable) {
                    try {
                        // Wait for onFrameAvailable() to signal us.  Use a timeout to avoid
                        // stalling the test if it doesn't arrive.
                        mFrameSyncObject.wait(TIMEOUT_MS);
                        if (!mFrameAvailable) {
                            // TODO: if "spurious wakeup", continue while loop
                            throw new RuntimeException("Camera frame wait timed out");
                        }
                    } catch (InterruptedException ie) {
                        // shouldn't happen
                        throw new RuntimeException(ie);
                    }
                }
                mFrameAvailable = false;
            }

            // Latch the data.
            mTextureRender.checkGlError("before updateTexImage");
            mSurfaceTexture.updateTexImage();
        }

        /**
         * Draws the data from SurfaceTexture onto the current EGL surface.
         */
        public void drawImage() {
            mTextureRender.drawFrame(mSurfaceTexture);
        }

        @Override
        public void onFrameAvailable(SurfaceTexture st) {
            if (VERBOSE) Log.d(TAG, "new frame available");
            synchronized (mFrameSyncObject) {
                if (mFrameAvailable) {
                    throw new RuntimeException("mFrameAvailable already set, frame could be dropped");
                }
                mFrameAvailable = true;
                mFrameSyncObject.notifyAll();
            }
        }
    }


    /**
     * Code for rendering a texture onto a surface using OpenGL ES 2.0.
     */
    private static class STextureRender {
        private static final int FLOAT_SIZE_BYTES = 4;
        private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
        private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
        private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
        private final float[] mTriangleVerticesData = {
                // X, Y, Z, U, V
                -1.0f, -1.0f, 0, 0.f, 0.f,
                 1.0f, -1.0f, 0, 1.f, 0.f,
                -1.0f,  1.0f, 0, 0.f, 1.f,
                 1.0f,  1.0f, 0, 1.f, 1.f,
        };

        private FloatBuffer mTriangleVertices;

        private static final String VERTEX_SHADER =
                "uniform mat4 uMVPMatrix;\n" +
                "uniform mat4 uSTMatrix;\n" +
                "attribute vec4 aPosition;\n" +
                "attribute vec4 aTextureCoord;\n" +
                "varying vec2 vTextureCoord;\n" +
                "void main() {\n" +
                "    gl_Position = uMVPMatrix * aPosition;\n" +
                "    vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n" +
                "}\n";

        private static final String FRAGMENT_SHADER =
                "#extension GL_OES_EGL_image_external : require\n" +
                "precision mediump float;\n" +      // highp here doesn't seem to matter
                "varying vec2 vTextureCoord;\n" +
                "uniform samplerExternalOES sTexture;\n" +
                "void main() {\n" +
                "    gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
                "}\n";

        private float[] mMVPMatrix = new float[16];
        private float[] mSTMatrix = new float[16];

        private int mProgram;
        private int mTextureID = -12345;
        private int muMVPMatrixHandle;
        private int muSTMatrixHandle;
        private int maPositionHandle;
        private int maTextureHandle;

        public STextureRender() {
            mTriangleVertices = ByteBuffer.allocateDirect(
                    mTriangleVerticesData.length * FLOAT_SIZE_BYTES)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            mTriangleVertices.put(mTriangleVerticesData).position(0);

            Matrix.setIdentityM(mSTMatrix, 0);
        }

        public int getTextureId() {
            return mTextureID;
        }

        public void drawFrame(SurfaceTexture st) {
            checkGlError("onDrawFrame start");
            st.getTransformMatrix(mSTMatrix);

            // (optional) clear to green so we can see if we're failing to set pixels
            GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            GLES20.glUseProgram(mProgram);
            checkGlError("glUseProgram");

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID);

            mTriangleVertices.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
            GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false,
                    TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mTriangleVertices);
            checkGlError("glVertexAttribPointer maPosition");
            GLES20.glEnableVertexAttribArray(maPositionHandle);
            checkGlError("glEnableVertexAttribArray maPositionHandle");

            mTriangleVertices.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);
            GLES20.glVertexAttribPointer(maTextureHandle, 2, GLES20.GL_FLOAT, false,
                    TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mTriangleVertices);
            checkGlError("glVertexAttribPointer maTextureHandle");
            GLES20.glEnableVertexAttribArray(maTextureHandle);
            checkGlError("glEnableVertexAttribArray maTextureHandle");

            Matrix.setIdentityM(mMVPMatrix, 0);
            GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
            GLES20.glUniformMatrix4fv(muSTMatrixHandle, 1, false, mSTMatrix, 0);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
            checkGlError("glDrawArrays");

            // IMPORTANT: on some devices, if you are sharing the external texture between two
            // contexts, one context may not see updates to the texture unless you un-bind and
            // re-bind it.  If you're not using shared EGL contexts, you don't need to bind
            // texture 0 here.
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        }

        /**
         * Initializes GL state.  Call this after the EGL surface has been created and made current.
         */
        public void surfaceCreated() {
            mProgram = createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
            if (mProgram == 0) {
                throw new RuntimeException("failed creating program");
            }
            maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
            checkLocation(maPositionHandle, "aPosition");
            maTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
            checkLocation(maTextureHandle, "aTextureCoord");

            muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
            checkLocation(muMVPMatrixHandle, "uMVPMatrix");
            muSTMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uSTMatrix");
            checkLocation(muSTMatrixHandle, "uSTMatrix");

            int[] textures = new int[1];
            GLES20.glGenTextures(1, textures, 0);

            mTextureID = textures[0];
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID);
            checkGlError("glBindTexture mTextureID");

            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S,
                    GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T,
                    GLES20.GL_CLAMP_TO_EDGE);
            checkGlError("glTexParameter");
        }

        /**
         * Replaces the fragment shader.  Pass in null to reset to default.
         */
        public void changeFragmentShader(String fragmentShader) {
            if (fragmentShader == null) {
                fragmentShader = FRAGMENT_SHADER;
            }
            GLES20.glDeleteProgram(mProgram);
            mProgram = createProgram(VERTEX_SHADER, fragmentShader);
            if (mProgram == 0) {
                throw new RuntimeException("failed creating program");
            }
        }

        private int loadShader(int shaderType, String source) {
            int shader = GLES20.glCreateShader(shaderType);
            checkGlError("glCreateShader type=" + shaderType);
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e(TAG, "Could not compile shader " + shaderType + ":");
                Log.e(TAG, " " + GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
            return shader;
        }

        private int createProgram(String vertexSource, String fragmentSource) {
            int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
            if (vertexShader == 0) {
                return 0;
            }
            int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
            if (pixelShader == 0) {
                return 0;
            }

            int program = GLES20.glCreateProgram();
            if (program == 0) {
                Log.e(TAG, "Could not create program");
            }
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program: ");
                Log.e(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
            return program;
        }

        public void checkGlError(String op) {
            int error;
            while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
                Log.e(TAG, op + ": glError " + error);
                throw new RuntimeException(op + ": glError " + error);
            }
        }

        public static void checkLocation(int location, String label) {
            if (location < 0) {
                throw new RuntimeException("Unable to locate '" + label + "' in program");
            }
        }
    }
}
