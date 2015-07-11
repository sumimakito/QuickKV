./gradlew --stacktrace clean build
GITHUB_TAG=Snapshot_${TRAVIS_BRANCH}_Build.${TRAVIS_BUILD_NUMBER}
cwd="`pwd`"
cd ./library/build/libs
sources=`ls | grep sources.jar`
javadoc=`ls | grep javadoc.jar`
mv ${sources} QuickKV_${GITHUB_TAG}_source.jar
mv ${javadoc} QuickKV_${GITHUB_TAG}_javadoc.jar
cd ${cwd}
