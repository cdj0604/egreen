/*
 *  base64.c
 *  UniSignURLScheme
 *
 *  Created by nhkim on 11. 4. 6..
 *  Copyright 2011 __MyCompanyName__. All rights reserved.
 *
 */

#include "base64.h"


char ntc(unsigned char n)
{
    if (n<26) return 'A'+n;
    if (n<52) return 'a'-26+n;
    if (n<62) return '0'-52+n;
    if (n==62) return '+';
    return '/';
}

unsigned char ctn(char c)
{
    if (c=='/') return 63;
    if (c=='+') return 62;
    if ((c>='A')&&(c<='Z')) return c-'A';
    if ((c>='a')&&(c<='z')) return c-'a'+26;
    if ((c>='0')&&(c<='9')) return c-'0'+52;
    if (c=='=') return 80;
    return 100;
}

int base64Encode( unsigned char *pData, int nDataLen, char** ppszB64Encode)
{
    // 3 8bit numbers become four characters 
    int nRv = 0;
    int i =0;
    char *pB64OutPtr=NULL;
    int quads = 0;
    int qc=0;  // Quadcount
    unsigned char c;
    unsigned char d;
	
    if((pData == NULL) || nDataLen == 0)
        return -1;
    
    *ppszB64Encode = (char*)malloc((nDataLen*2) + 1);
	memset(*ppszB64Encode, 0x00, (nDataLen*2)+1);
	pB64OutPtr = *ppszB64Encode;
    
    while(i<nDataLen)
    {
        c=pData[i];
        *pB64OutPtr++=ntc((unsigned char)(c/4));
        c=c*64;
        
        i++;
        
        if (i>=nDataLen) 
        {
            *pB64OutPtr++=ntc((unsigned char)(c/4));
            *pB64OutPtr++='=';
            *pB64OutPtr++='=';
            break;
        }
        d=pData[i];
        *pB64OutPtr++=ntc((unsigned char)(c/4+d/16));
        d=d*16;
        
        i++;
        
        if (i>=nDataLen) 
        {
            *pB64OutPtr++=ntc((unsigned char)(d/4));
            *pB64OutPtr++='=';
            break;
        }
        c=pData[i];
        *pB64OutPtr++=ntc((unsigned char)(d/4+c/64));
        c=c*4;
        
        i++;
        
        *pB64OutPtr++=ntc((unsigned char)(c/4));
        
        
        qc++; /* qz will never be zero, quads = 0 means no linebreaks */
        if (qc==quads)
        {
            *pB64OutPtr++='\n';
            qc=0;
        }
        
    }
    
    /* if ((quads!=0)&&(qc!=0)) *pB64Data++='\n'; */ /* Insert last linebreak */
    return nRv;
}



int base64Decode(char *pszB64Encode, unsigned char** ppData, int* pnDataLen)
{
    int nRv = 0;
    unsigned char c,d,e,f;
    char A,B,C;
    int i;
    int add;
    unsigned char *pDataPtr=NULL;
    int nB64DataLen = 0;
	
    if(pszB64Encode == NULL)
        return -1;
	
    nB64DataLen = strlen(pszB64Encode);
	
    *ppData = (unsigned char*)malloc((nB64DataLen+1)+1);
    memset(*ppData, 0x00, (nB64DataLen+1)+1);
	
    pDataPtr = *ppData;
    
    for (i=0;i+3<nB64DataLen;){
        add=0;
        A=B=C=0;
        c=d=e=f=100;
        
        while ((c==100)&&(i<nB64DataLen))    c=ctn(pszB64Encode[i++]);
        while ((d==100)&&(i<nB64DataLen))    d=ctn(pszB64Encode[i++]);
        while ((e==100)&&(i<nB64DataLen))    e=ctn(pszB64Encode[i++]);
        while ((f==100)&&(i<nB64DataLen))    f=ctn(pszB64Encode[i++]);
        
        if (f==100) return -1; /* Not valid end */
        
        if (c<64) {
            A+=(c*4);
            if (d<64) 
            {
                A+=d/16;                
                B+=(d*16);
                
                if (e<64) 
                {
                    B+=e/4;
                    C+=(e*64);
                    
                    if (f<64) 
                    {
                        C+=f;
                        pDataPtr[2]=C;
                        add+=1;
                        
                    }
                    pDataPtr[1]=B;
                    add+=1;
                    
                }
                pDataPtr[0]=A;
                add+=1;
            }
        }
        pDataPtr+=add;
        
        if (f==80) break; /* end because '=' encountered */
    }
    
    *pnDataLen = pDataPtr - *ppData;
    
    return nRv;
}

