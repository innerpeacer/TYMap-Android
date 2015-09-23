/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.7
 *
 * This file is not intended to be easily readable and contains a number of
 * coding conventions designed to improve portability and efficiency. Do not make
 * changes to this file unless you know what you are doing--modify the SWIG
 * interface file instead.
 * ----------------------------------------------------------------------------- */

#define SWIGJAVA


#ifdef __cplusplus
/* SwigValueWrapper is described in swig.swg */
template<typename T> class SwigValueWrapper {
  struct SwigMovePointer {
    T *ptr;
    SwigMovePointer(T *p) : ptr(p) { }
    ~SwigMovePointer() { delete ptr; }
    SwigMovePointer& operator=(SwigMovePointer& rhs) { T* oldptr = ptr; ptr = 0; delete oldptr; ptr = rhs.ptr; rhs.ptr = 0; return *this; }
  } pointer;
  SwigValueWrapper& operator=(const SwigValueWrapper<T>& rhs);
  SwigValueWrapper(const SwigValueWrapper<T>& rhs);
public:
  SwigValueWrapper() : pointer(0) { }
  SwigValueWrapper& operator=(const T& t) { SwigMovePointer tmp(new T(t)); pointer = tmp; return *this; }
  operator T&() const { return *pointer.ptr; }
  T *operator&() { return pointer.ptr; }
};

template <typename T> T SwigValueInit() {
  return T();
}
#endif

/* -----------------------------------------------------------------------------
 *  This section contains generic SWIG labels for method/variable
 *  declarations/attributes, and other compiler dependent labels.
 * ----------------------------------------------------------------------------- */

/* template workaround for compilers that cannot correctly implement the C++ standard */
#ifndef SWIGTEMPLATEDISAMBIGUATOR
# if defined(__SUNPRO_CC) && (__SUNPRO_CC <= 0x560)
#  define SWIGTEMPLATEDISAMBIGUATOR template
# elif defined(__HP_aCC)
/* Needed even with `aCC -AA' when `aCC -V' reports HP ANSI C++ B3910B A.03.55 */
/* If we find a maximum version that requires this, the test would be __HP_aCC <= 35500 for A.03.55 */
#  define SWIGTEMPLATEDISAMBIGUATOR template
# else
#  define SWIGTEMPLATEDISAMBIGUATOR
# endif
#endif

/* inline attribute */
#ifndef SWIGINLINE
# if defined(__cplusplus) || (defined(__GNUC__) && !defined(__STRICT_ANSI__))
#   define SWIGINLINE inline
# else
#   define SWIGINLINE
# endif
#endif

/* attribute recognised by some compilers to avoid 'unused' warnings */
#ifndef SWIGUNUSED
# if defined(__GNUC__)
#   if !(defined(__cplusplus)) || (__GNUC__ > 3 || (__GNUC__ == 3 && __GNUC_MINOR__ >= 4))
#     define SWIGUNUSED __attribute__ ((__unused__))
#   else
#     define SWIGUNUSED
#   endif
# elif defined(__ICC)
#   define SWIGUNUSED __attribute__ ((__unused__))
# else
#   define SWIGUNUSED
# endif
#endif

#ifndef SWIG_MSC_UNSUPPRESS_4505
# if defined(_MSC_VER)
#   pragma warning(disable : 4505) /* unreferenced local function has been removed */
# endif
#endif

#ifndef SWIGUNUSEDPARM
# ifdef __cplusplus
#   define SWIGUNUSEDPARM(p)
# else
#   define SWIGUNUSEDPARM(p) p SWIGUNUSED
# endif
#endif

/* internal SWIG method */
#ifndef SWIGINTERN
# define SWIGINTERN static SWIGUNUSED
#endif

/* internal inline SWIG method */
#ifndef SWIGINTERNINLINE
# define SWIGINTERNINLINE SWIGINTERN SWIGINLINE
#endif

/* exporting methods */
#if (__GNUC__ >= 4) || (__GNUC__ == 3 && __GNUC_MINOR__ >= 4)
#  ifndef GCC_HASCLASSVISIBILITY
#    define GCC_HASCLASSVISIBILITY
#  endif
#endif

#ifndef SWIGEXPORT
# if defined(_WIN32) || defined(__WIN32__) || defined(__CYGWIN__)
#   if defined(STATIC_LINKED)
#     define SWIGEXPORT
#   else
#     define SWIGEXPORT __declspec(dllexport)
#   endif
# else
#   if defined(__GNUC__) && defined(GCC_HASCLASSVISIBILITY)
#     define SWIGEXPORT __attribute__ ((visibility("default")))
#   else
#     define SWIGEXPORT
#   endif
# endif
#endif

/* calling conventions for Windows */
#ifndef SWIGSTDCALL
# if defined(_WIN32) || defined(__WIN32__) || defined(__CYGWIN__)
#   define SWIGSTDCALL __stdcall
# else
#   define SWIGSTDCALL
# endif
#endif

/* Deal with Microsoft's attempt at deprecating C standard runtime functions */
#if !defined(SWIG_NO_CRT_SECURE_NO_DEPRECATE) && defined(_MSC_VER) && !defined(_CRT_SECURE_NO_DEPRECATE)
# define _CRT_SECURE_NO_DEPRECATE
#endif

/* Deal with Microsoft's attempt at deprecating methods in the standard C++ library */
#if !defined(SWIG_NO_SCL_SECURE_NO_DEPRECATE) && defined(_MSC_VER) && !defined(_SCL_SECURE_NO_DEPRECATE)
# define _SCL_SECURE_NO_DEPRECATE
#endif

/* Deal with Apple's deprecated 'AssertMacros.h' from Carbon-framework */
#if defined(__APPLE__) && !defined(__ASSERT_MACROS_DEFINE_VERSIONS_WITHOUT_UNDERSCORES)
# define __ASSERT_MACROS_DEFINE_VERSIONS_WITHOUT_UNDERSCORES 0
#endif

/* Intel's compiler complains if a variable which was never initialised is
 * cast to void, which is a common idiom which we use to indicate that we
 * are aware a variable isn't used.  So we just silence that warning.
 * See: https://github.com/swig/swig/issues/192 for more discussion.
 */
#ifdef __INTEL_COMPILER
# pragma warning disable 592
#endif


/* Fix for jlong on some versions of gcc on Windows */
#if defined(__GNUC__) && !defined(__INTEL_COMPILER)
  typedef long long __int64;
#endif

/* Fix for jlong on 64-bit x86 Solaris */
#if defined(__x86_64)
# ifdef _LP64
#   undef _LP64
# endif
#endif

#include <jni.h>
#include <stdlib.h>
#include <string.h>


/* Support for throwing Java exceptions */
typedef enum {
  SWIG_JavaOutOfMemoryError = 1, 
  SWIG_JavaIOException, 
  SWIG_JavaRuntimeException, 
  SWIG_JavaIndexOutOfBoundsException,
  SWIG_JavaArithmeticException,
  SWIG_JavaIllegalArgumentException,
  SWIG_JavaNullPointerException,
  SWIG_JavaDirectorPureVirtual,
  SWIG_JavaUnknownError
} SWIG_JavaExceptionCodes;

typedef struct {
  SWIG_JavaExceptionCodes code;
  const char *java_exception;
} SWIG_JavaExceptions_t;


static void SWIGUNUSED SWIG_JavaThrowException(JNIEnv *jenv, SWIG_JavaExceptionCodes code, const char *msg) {
  jclass excep;
  static const SWIG_JavaExceptions_t java_exceptions[] = {
    { SWIG_JavaOutOfMemoryError, "java/lang/OutOfMemoryError" },
    { SWIG_JavaIOException, "java/io/IOException" },
    { SWIG_JavaRuntimeException, "java/lang/RuntimeException" },
    { SWIG_JavaIndexOutOfBoundsException, "java/lang/IndexOutOfBoundsException" },
    { SWIG_JavaArithmeticException, "java/lang/ArithmeticException" },
    { SWIG_JavaIllegalArgumentException, "java/lang/IllegalArgumentException" },
    { SWIG_JavaNullPointerException, "java/lang/NullPointerException" },
    { SWIG_JavaDirectorPureVirtual, "java/lang/RuntimeException" },
    { SWIG_JavaUnknownError,  "java/lang/UnknownError" },
    { (SWIG_JavaExceptionCodes)0,  "java/lang/UnknownError" }
  };
  const SWIG_JavaExceptions_t *except_ptr = java_exceptions;

  while (except_ptr->code != code && except_ptr->code)
    except_ptr++;

  jenv->ExceptionClear();
  excep = jenv->FindClass(except_ptr->java_exception);
  if (excep)
    jenv->ThrowNew(excep, msg);
}


/* Contract support */

#define SWIG_contract_assert(nullreturn, expr, msg) if (!(expr)) {SWIG_JavaThrowException(jenv, SWIG_JavaIllegalArgumentException, msg); return nullreturn; } else


#include <string>



#include "TYMapSDK/src/Utils/IPEncryption.hpp"
#include "TYMapSDK/src/Utils/MD5Utils.hpp"
#include "TYMapSDK/src/Utils/IPLicenseValidation.h"

using namespace std;



#ifdef __cplusplus
extern "C" {
#endif

SWIGEXPORT jstring JNICALL Java_com_ty_mapsdk_IPMapSDKJNI_md5(JNIEnv *jenv, jclass jcls, jstring jarg1) {
  jstring jresult = 0 ;
  std::string arg1 ;
  std::string result;
  
  (void)jenv;
  (void)jcls;
  if(!jarg1) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return 0;
  } 
  const char *arg1_pstr = (const char *)jenv->GetStringUTFChars(jarg1, 0); 
  if (!arg1_pstr) return 0;
  (&arg1)->assign(arg1_pstr);
  jenv->ReleaseStringUTFChars(jarg1, arg1_pstr); 
  result = md5(arg1);
  jresult = jenv->NewStringUTF((&result)->c_str()); 
  return jresult;
}


SWIGEXPORT jstring JNICALL Java_com_ty_mapsdk_IPMapSDKJNI_decryptString_1_1SWIG_10(JNIEnv *jenv, jclass jcls, jstring jarg1) {
  jstring jresult = 0 ;
  std::string arg1 ;
  std::string result;
  
  (void)jenv;
  (void)jcls;
  if(!jarg1) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return 0;
  } 
  const char *arg1_pstr = (const char *)jenv->GetStringUTFChars(jarg1, 0); 
  if (!arg1_pstr) return 0;
  (&arg1)->assign(arg1_pstr);
  jenv->ReleaseStringUTFChars(jarg1, arg1_pstr); 
  result = decryptString(arg1);
  jresult = jenv->NewStringUTF((&result)->c_str()); 
  return jresult;
}


SWIGEXPORT jstring JNICALL Java_com_ty_mapsdk_IPMapSDKJNI_encryptString_1_1SWIG_10(JNIEnv *jenv, jclass jcls, jstring jarg1) {
  jstring jresult = 0 ;
  std::string arg1 ;
  std::string result;
  
  (void)jenv;
  (void)jcls;
  if(!jarg1) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return 0;
  } 
  const char *arg1_pstr = (const char *)jenv->GetStringUTFChars(jarg1, 0); 
  if (!arg1_pstr) return 0;
  (&arg1)->assign(arg1_pstr);
  jenv->ReleaseStringUTFChars(jarg1, arg1_pstr); 
  result = encryptString(arg1);
  jresult = jenv->NewStringUTF((&result)->c_str()); 
  return jresult;
}


SWIGEXPORT jstring JNICALL Java_com_ty_mapsdk_IPMapSDKJNI_decryptFile_1_1SWIG_10(JNIEnv *jenv, jclass jcls, jstring jarg1) {
  jstring jresult = 0 ;
  char *arg1 = (char *) 0 ;
  std::string result;
  
  (void)jenv;
  (void)jcls;
  arg1 = 0;
  if (jarg1) {
    arg1 = (char *)jenv->GetStringUTFChars(jarg1, 0);
    if (!arg1) return 0;
  }
  result = decryptFile((char const *)arg1);
  jresult = jenv->NewStringUTF((&result)->c_str()); 
  if (arg1) jenv->ReleaseStringUTFChars(jarg1, (const char *)arg1);
  return jresult;
}


SWIGEXPORT jstring JNICALL Java_com_ty_mapsdk_IPMapSDKJNI_decryptString_1_1SWIG_11(JNIEnv *jenv, jclass jcls, jstring jarg1, jstring jarg2) {
  jstring jresult = 0 ;
  std::string arg1 ;
  std::string arg2 ;
  std::string result;
  
  (void)jenv;
  (void)jcls;
  if(!jarg1) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return 0;
  } 
  const char *arg1_pstr = (const char *)jenv->GetStringUTFChars(jarg1, 0); 
  if (!arg1_pstr) return 0;
  (&arg1)->assign(arg1_pstr);
  jenv->ReleaseStringUTFChars(jarg1, arg1_pstr); 
  if(!jarg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return 0;
  } 
  const char *arg2_pstr = (const char *)jenv->GetStringUTFChars(jarg2, 0); 
  if (!arg2_pstr) return 0;
  (&arg2)->assign(arg2_pstr);
  jenv->ReleaseStringUTFChars(jarg2, arg2_pstr); 
  result = decryptString(arg1,arg2);
  jresult = jenv->NewStringUTF((&result)->c_str()); 
  return jresult;
}


SWIGEXPORT jstring JNICALL Java_com_ty_mapsdk_IPMapSDKJNI_encryptString_1_1SWIG_11(JNIEnv *jenv, jclass jcls, jstring jarg1, jstring jarg2) {
  jstring jresult = 0 ;
  std::string arg1 ;
  std::string arg2 ;
  std::string result;
  
  (void)jenv;
  (void)jcls;
  if(!jarg1) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return 0;
  } 
  const char *arg1_pstr = (const char *)jenv->GetStringUTFChars(jarg1, 0); 
  if (!arg1_pstr) return 0;
  (&arg1)->assign(arg1_pstr);
  jenv->ReleaseStringUTFChars(jarg1, arg1_pstr); 
  if(!jarg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return 0;
  } 
  const char *arg2_pstr = (const char *)jenv->GetStringUTFChars(jarg2, 0); 
  if (!arg2_pstr) return 0;
  (&arg2)->assign(arg2_pstr);
  jenv->ReleaseStringUTFChars(jarg2, arg2_pstr); 
  result = encryptString(arg1,arg2);
  jresult = jenv->NewStringUTF((&result)->c_str()); 
  return jresult;
}


SWIGEXPORT void JNICALL Java_com_ty_mapsdk_IPMapSDKJNI_encryptFile(JNIEnv *jenv, jclass jcls, jstring jarg1, jstring jarg2, jstring jarg3) {
  char *arg1 = (char *) 0 ;
  char *arg2 = (char *) 0 ;
  char *arg3 = (char *) 0 ;
  
  (void)jenv;
  (void)jcls;
  arg1 = 0;
  if (jarg1) {
    arg1 = (char *)jenv->GetStringUTFChars(jarg1, 0);
    if (!arg1) return ;
  }
  arg2 = 0;
  if (jarg2) {
    arg2 = (char *)jenv->GetStringUTFChars(jarg2, 0);
    if (!arg2) return ;
  }
  arg3 = 0;
  if (jarg3) {
    arg3 = (char *)jenv->GetStringUTFChars(jarg3, 0);
    if (!arg3) return ;
  }
  encryptFile((char const *)arg1,(char const *)arg2,(char const *)arg3);
  if (arg1) jenv->ReleaseStringUTFChars(jarg1, (const char *)arg1);
  if (arg2) jenv->ReleaseStringUTFChars(jarg2, (const char *)arg2);
  if (arg3) jenv->ReleaseStringUTFChars(jarg3, (const char *)arg3);
}


SWIGEXPORT jstring JNICALL Java_com_ty_mapsdk_IPMapSDKJNI_decryptFile_1_1SWIG_11(JNIEnv *jenv, jclass jcls, jstring jarg1, jstring jarg2) {
  jstring jresult = 0 ;
  char *arg1 = (char *) 0 ;
  char *arg2 = (char *) 0 ;
  std::string result;
  
  (void)jenv;
  (void)jcls;
  arg1 = 0;
  if (jarg1) {
    arg1 = (char *)jenv->GetStringUTFChars(jarg1, 0);
    if (!arg1) return 0;
  }
  arg2 = 0;
  if (jarg2) {
    arg2 = (char *)jenv->GetStringUTFChars(jarg2, 0);
    if (!arg2) return 0;
  }
  result = decryptFile((char const *)arg1,(char const *)arg2);
  jresult = jenv->NewStringUTF((&result)->c_str()); 
  if (arg1) jenv->ReleaseStringUTFChars(jarg1, (const char *)arg1);
  if (arg2) jenv->ReleaseStringUTFChars(jarg2, (const char *)arg2);
  return jresult;
}


SWIGEXPORT jboolean JNICALL Java_com_ty_mapsdk_IPMapSDKJNI_checkValidity(JNIEnv *jenv, jclass jcls, jstring jarg1, jstring jarg2, jstring jarg3) {
  jboolean jresult = 0 ;
  std::string arg1 ;
  std::string arg2 ;
  std::string arg3 ;
  bool result;
  
  (void)jenv;
  (void)jcls;
  if(!jarg1) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return 0;
  } 
  const char *arg1_pstr = (const char *)jenv->GetStringUTFChars(jarg1, 0); 
  if (!arg1_pstr) return 0;
  (&arg1)->assign(arg1_pstr);
  jenv->ReleaseStringUTFChars(jarg1, arg1_pstr); 
  if(!jarg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return 0;
  } 
  const char *arg2_pstr = (const char *)jenv->GetStringUTFChars(jarg2, 0); 
  if (!arg2_pstr) return 0;
  (&arg2)->assign(arg2_pstr);
  jenv->ReleaseStringUTFChars(jarg2, arg2_pstr); 
  if(!jarg3) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return 0;
  } 
  const char *arg3_pstr = (const char *)jenv->GetStringUTFChars(jarg3, 0); 
  if (!arg3_pstr) return 0;
  (&arg3)->assign(arg3_pstr);
  jenv->ReleaseStringUTFChars(jarg3, arg3_pstr); 
  result = (bool)checkValidity(arg1,arg2,arg3);
  jresult = (jboolean)result; 
  return jresult;
}


SWIGEXPORT jstring JNICALL Java_com_ty_mapsdk_IPMapSDKJNI_getExpiredDate(JNIEnv *jenv, jclass jcls, jstring jarg1, jstring jarg2, jstring jarg3) {
  jstring jresult = 0 ;
  std::string arg1 ;
  std::string arg2 ;
  std::string arg3 ;
  std::string result;
  
  (void)jenv;
  (void)jcls;
  if(!jarg1) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return 0;
  } 
  const char *arg1_pstr = (const char *)jenv->GetStringUTFChars(jarg1, 0); 
  if (!arg1_pstr) return 0;
  (&arg1)->assign(arg1_pstr);
  jenv->ReleaseStringUTFChars(jarg1, arg1_pstr); 
  if(!jarg2) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return 0;
  } 
  const char *arg2_pstr = (const char *)jenv->GetStringUTFChars(jarg2, 0); 
  if (!arg2_pstr) return 0;
  (&arg2)->assign(arg2_pstr);
  jenv->ReleaseStringUTFChars(jarg2, arg2_pstr); 
  if(!jarg3) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null string");
    return 0;
  } 
  const char *arg3_pstr = (const char *)jenv->GetStringUTFChars(jarg3, 0); 
  if (!arg3_pstr) return 0;
  (&arg3)->assign(arg3_pstr);
  jenv->ReleaseStringUTFChars(jarg3, arg3_pstr); 
  result = getExpiredDate(arg1,arg2,arg3);
  jresult = jenv->NewStringUTF((&result)->c_str()); 
  return jresult;
}


#ifdef __cplusplus
}
#endif

