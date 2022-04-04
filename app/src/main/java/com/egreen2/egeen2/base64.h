/*
 *  base64.h
 *  UniSignURLScheme
 *
 *  Created by nhkim on 11. 4. 6..
 *  Copyright 2011 __MyCompanyName__. All rights reserved.
 *
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char ntc(unsigned char n);
unsigned char ctn(char c);
int base64Encode( unsigned char *pData, int nDataLen, char** ppszB64Encode);
int base64Decode(char *pszB64Encode, unsigned char** ppData, int* pnDataLen);
