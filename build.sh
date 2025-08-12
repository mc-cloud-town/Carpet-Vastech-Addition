#!/bin/bash

./gradlew setupCarpetmod
./gradlew createRelease

cd ~/Carpet-Vastech-Addition/ || exit

rm -rf build/tmp/fullRelease
mkdir -p build/tmp/fullRelease

cd build/tmp/fullRelease || exit
cp ~/.gradle/caches/minecraft/net/minecraft/minecraft_server/1.12.2/minecraft_server-1.12.2.jar ./base.jar
cp ~/.gradle/caches/modules-2/files-2.1/com.thoughtworks.paranamer/paranamer/2.6/52c3c8d8876440d714e23036eb87bcc4244d9aa5/paranamer-2.6.jar .
cp ~/.gradle/caches/modules-2/files-2.1/com.sk89q.worldedit/worldedit-core/6.1/ecedf9725babeb10e94baac407cdf175716f88f0/worldedit-core-6.1.jar .

mkdir -p paranamer worldedit carpetmod

echo "📦 Extracting paranamer-2.6.jar to paranamer directory..."
unzip -x META-INF/MANIFEST.MF -qo paranamer-2.6.jar -d paranamer

echo "📦 Extracting worldedit-core-6.1.jar to worldedit directory..."
unzip -x META-INF/MANIFEST.MF -qo worldedit-core-6.1.jar -d worldedit

echo "📦 Extracting VasCM-CTEC zip to carpetmod directory..."
unzip -x META-INF/MANIFEST.MF -qo ../../distributions/Carpetmod_dev.zip -d carpetmod

echo "🔄 Updating base.jar with paranamer files..."
zip -ur base.jar paranamer/*

echo "🔄 Updating base.jar with worldedit files..."
zip -ur base.jar worldedit/*

echo "🔄 Updating base.jar with Carpetmod files..."
zip -ur base.jar carpetmod/*

echo "✅ base.jar update completed! Preparing proxy folder..."

mkdir -p proxy/in proxy/out

cd proxy || exit

cp ../../../distributions/Carpetmod_dev.zip ./out/patcher.zip
cp ../base.jar ./in/vascm_ctec_"${VERSION}"_proxy.jar
cp ../base.jar ./out/vascm_ctec_"${VERSION}".jar
cp ../base.jar ./out/base.zip

echo "🚀 Running VanillaCord patcher..."
java -jar ../../../../.github/workflows/scripts/VanillaCord.jar vascm_ctec_"${VERSION}"_proxy

mkdir ../../../out
cp ./out/* ../../../out
