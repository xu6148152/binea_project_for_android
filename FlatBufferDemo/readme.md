###Build FlatBuffer

1. git clone https://github.com/google/flatbuffers.git

2. cd flatbuffers

3. select compiler

    $ cmake -G "Unix Makefiles"
    $ cmake -G "Visual Studio 10"
    $ cmake -G "Xcode"

4. build for your platform as usual.

5. create schema file with extension.fbs

6. ./flatc -j -b xxx.fbs xxx.json it will generate java files

