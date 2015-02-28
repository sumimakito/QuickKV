./gradlew clean build
cd ./library/build/libs
source=@`ls | grep source.jar`
javadoc=@`ls | grep javadoc.jar`
curl -F "file=${source}" http://repo.keep.moe/api/travis/push/qkv.php
curl -F "file=${javadoc}" http://repo.keep.moe/api/travis/push/qkv.php