#include "ndkdemo_binea_com_ndkdemo_MainActivity.h"

JNIEXPORT jstring JNICALL Java_ndkdemo_binea_com_ndkdemo_MainActivity_hellojni
  (JNIEnv* env, jobject obj)
{
    return env->NewStringUTF( "Hello from JNI !  Compiled with ABI .");
}