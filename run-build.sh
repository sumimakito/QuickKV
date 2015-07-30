./gradlew --stacktrace clean build
./gradlew fatJar
GITHUB_TAG=Snapshot_${TRAVIS_BRANCH}_Build.${TRAVIS_BUILD_NUMBER}
cwd="`pwd`"
cd ./library/build/libs
#sources=`ls | grep sources.jar`
#javadoc=`ls | grep javadoc.jar`
#mv ${sources} QuickKV_${GITHUB_TAG}_sources.jar
#mv ${javadoc} QuickKV_${GITHUB_TAG}_javadoc.jar
mv QuickKV.jar QuickKV_${GITHUB_TAG}.jar
cd ${cwd}
