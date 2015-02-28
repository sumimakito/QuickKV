./gradlew clean build
cd ./library/build/libs
curl -F "file=@`ls | grep source.jar`" http://repo.keep.moe/api/travis/push/qkv.php
curl -F "file=@`ls | grep javadoc.jar`" http://repo.keep.moe/api/travis/push/qkv.php