./gradlew --stacktrace clean build
GITHUB_TAG=Snapshot_${TRAVIS_BRANCH}_Build.${TRAVIS_BUILD_NUMBER}
cwd="`pwd`"
cd ./library/build/intermediates/bundles/release
#sources=`ls | grep sources.jar`
#javadoc=`ls | grep javadoc.jar`
mv classes.jar QuickKV_${GITHUB_TAG}.jar
#mv ${javadoc} QuickKV_${GITHUB_TAG}_javadoc.jar
cd ${cwd}
