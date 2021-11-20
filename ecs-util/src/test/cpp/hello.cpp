//
// Created by Admin on 2019/8/5.
//


#include "com_gavin_app_jvm_jni_JniUtil.h"

JNIEXPORT jstring JNICALL Java_com_com_gavin_app_jvm_jni_JniUtil_sayHelloWorld

  (JNIEnv *env, jobject){

      return env->NewStringUTF("Hello,World!");
  }


