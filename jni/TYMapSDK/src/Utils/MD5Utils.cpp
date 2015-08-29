/*
 * MD5Utils.cpp
 *
 *  Created on: 2015-8-30
 *      Author: innerpeacer
 */

#include "MD5Utils.hpp"
#include "MD5.hpp"

std::string md5(std::string str)
{
    MD5 md5(str.c_str());
    return md5.toString();
}

