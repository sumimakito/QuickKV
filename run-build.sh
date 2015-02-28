./gradlew clean build
sources=@`ls | grep sources.jar`
javadoc=@`ls | grep javadoc.jar`
curl -F "file=${sources}" http://repo.keep.moe/api/travis/push/qkv.php
curl -F "file=${javadoc}" http://repo.keep.moe/api/travis/push/qkv.php