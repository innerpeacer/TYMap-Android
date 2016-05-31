//
//  MD5.hpp
//  TYDecryption
//
//  Created by innerpeacer on 15/7/21.
//  Copyright © 2015年 innerpeacer. All rights reserved.
//

#ifndef MD5_cpp
#define MD5_cpp


#include <string>
#include <fstream>

typedef unsigned char byte;
typedef unsigned int uint32;

using std::string;
using std::ifstream;


namespace Innerpeacer {
    namespace MapSDK {

    }
}

class IPXMD5 {
public:
    IPXMD5();
    IPXMD5(const void *input, size_t length);
    IPXMD5(const string &str);
    IPXMD5(ifstream &in);
    void update(const void *input, size_t length);
    void update(const string &str);
    void update(ifstream &in);
    const byte* digest();
    string toString();
    void reset();
private:
    void update(const byte *input, size_t length);
    void final();
    void transform(const byte block[64]);
    void encode(const uint32 *input, byte *output, size_t length);
    void decode(const byte *input, uint32 *output, size_t length);
    string bytesToHexString(const byte *input, size_t length);

    /* class uncopyable */
    IPXMD5(const IPXMD5&);
    IPXMD5& operator=(const IPXMD5&);
private:
    uint32 _state[4];	/* state (ABCD) */
    uint32 _count[2];	/* number of bits, modulo 2^64 (low-order word first) */
    byte _buffer[64];	/* input buffer */
    byte _digest[16];	/* message digest */
    bool _finished;		/* calculate finished ? */

    static const byte PADDING[64];	/* padding for calculate */
    static const char HEX[16];
    static const size_t BUFFER_SIZE = 1024;
};

#endif /* MD5_cpp */
