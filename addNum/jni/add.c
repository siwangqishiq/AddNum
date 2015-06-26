/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* This is a JNI example where we use native methods to play video
 * using OpenMAX AL. See the corresponding Java source file located at:
 *
 *   src/com/example/nativemedia/NativeMedia/NativeMedia.java
 *
 * In this example we use assert() for "impossible" error conditions,
 * and explicit handling and recovery for more likely error conditions.
 */
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "com_xinlan_addnum_Native.h"

#define SUCCESS             0
#define ERROR               -1
#define MAX_SIZE      2048

typedef struct _Node {
	int data;
	struct _Node *prior;
	struct _Node *next;
} Node;

typedef struct _Link {
	Node *head;
	Node *tail;
	int length;
} Link;

int convertCharToInt(char c) {
	return c - '0';
}

char convertIntToChar(int i) {
	return i + '0';
}

//������������ �����½��
//
int nodeValueAdd(Node *node1, Node *node2, int lowValueOverflow, Node **out,
		int *overflowValue) {
	if (node1 == NULL || node2 == NULL) {
		printf("error node1 == NULL || node2 == NULL \n");
		return ERROR;
	}
	Node *pRetNode = (Node *) malloc(sizeof(Node));
	if (pRetNode == NULL) {
		printf("error (Node *)malloc(sizeof(Node) \n");
		return ERROR;
	}

	//�������
	pRetNode->data = node1->data + node2->data + lowValueOverflow;
	if (pRetNode->data >= 10) { //�Ƿ������λ�ж�
		*overflowValue = 1;
		pRetNode->data -= 10;
	} else { //û�в�����λ
		*overflowValue = 0;
	} //end if

	pRetNode->next = NULL; //���ָ��NULL
	pRetNode->prior = NULL; //ǰ��ָ��NULL

	*out = pRetNode;
	return SUCCESS;
}

//�½�� ����ͷ��
int insertNodeToHead(Link *pLink, Node *pNode) {
	if (pLink == NULL || pNode == NULL) {
		printf("error pLink == NULL || pNode == NULL\n");
		return ERROR;
	}
	Node *p = pLink->head;
	if (p == NULL) { //��һ�β���
		pLink->head = pNode;
		pLink->tail = pNode;
	} else {
		pNode->next = p;
		p->prior = pNode;

		pLink->head = pNode;
	} //end if

	pLink->length++; // ����+1

	return SUCCESS;
}

//������Ӳ���
int addNum(Link *pNumOne, Link *pNumTwo, Link **result) {
	if (pNumOne == NULL || pNumTwo == NULL || pNumOne->length <= 0
			|| pNumTwo->length <= 0) {
		printf("error pNumOne== NULL or pNumTwo == NULL \n");
		return ERROR;
	}
	Link *pRetLink = (Link *) malloc(sizeof(Link));
	pRetLink->length = 0;
	pRetLink->head = NULL;
	pRetLink->tail = NULL;

	Node *pNode1 = pNumOne->tail; // ����1 ĩβ���
	Node *pNode2 = pNumTwo->tail; // ����2 ĩβ���

	int overflowValue = 0;
	while (pNode1 != NULL && pNode2 != NULL) {

		Node *pResultNode = NULL;

		if (nodeValueAdd(pNode1, pNode2, overflowValue, &pResultNode,
				&overflowValue) == SUCCESS) //�������ӳɹ�
		{

			if (insertNodeToHead(pRetLink, pResultNode) != SUCCESS) {
				printf("error insertNodeToHead() \n");
				return ERROR;
			}
		} else {
			printf("error nodeValueAdd() \n");
			return ERROR;
		} //end if

		pNode1 = pNode1->prior;
		pNode2 = pNode2->prior;
	} //end while

	//add the surplus for num1
	while (pNode1 != NULL) {
		Node *addNode = (Node *) malloc(sizeof(Node));
		if (addNode == NULL) {
			printf("error malloc extern Mode \n");
			return ERROR;
		}

		addNode->data = pNode1->data + overflowValue;
		if (overflowValue > 0) { //��λֵ  ��ս�λֵ
			overflowValue = 0;
		}
		if (addNode->data >= 10) { //��λ�ж�
			addNode->data -= 10;
			overflowValue++;
		}
		//addNode->data  = pNode1->data;
		addNode->next = NULL;
		addNode->prior = NULL;
		if (insertNodeToHead(pRetLink, addNode) != SUCCESS) {
			printf("error insertNodeToHead() \n");
			return ERROR;
		}

		pNode1 = pNode1->prior;
	} //end while

	// add surplus for num2
	while (pNode2 != NULL) {
		Node *addNode = (Node *) malloc(sizeof(Node));
		if (addNode == NULL) {
			printf("error malloc extern Mode \n");
			return ERROR;
		}
		addNode->data = pNode2->data + overflowValue;
		if (overflowValue > 0) { //��λֵ  ��ս�λֵ
			overflowValue = 0;
		}
		if (addNode->data >= 10) { //��λ�ж�
			addNode->data -= 10;
			overflowValue++;
		}
		addNode->next = NULL;
		addNode->prior = NULL;
		if (insertNodeToHead(pRetLink, addNode) != SUCCESS) {
			printf("error insertNodeToHead() \n");
			return ERROR;
		}

		pNode2 = pNode2->prior;
	} //end while

	//add extern Node
	if (overflowValue > 0) {
		Node *externNode = (Node *) malloc(sizeof(Node));
		if (externNode == NULL) {
			printf("error malloc extern Mode \n");
			return ERROR;
		}
		externNode->data = overflowValue;
		externNode->next = NULL;
		externNode->prior = NULL;
		if (insertNodeToHead(pRetLink, externNode) != SUCCESS) {
			printf("error insertNodeToHead() \n");
			return ERROR;
		}
		//printf("add extern head\n");
	}

	*result = pRetLink; //����ֵ

	return SUCCESS;
}

//����Link�ṹ��
int createLink(const char *numStr, Link **link) {
	const char *pStr = numStr;

	Link *tempLink = (Link *) malloc(sizeof(Link));
	if (tempLink == NULL) {
		printf("run  code '(Link *)malloc(sizeof(Link));' error \n");
		return ERROR;
	}

	tempLink->head = NULL;
	tempLink->tail = NULL;
	tempLink->length = 0;

	int index = 0;
	Node *pTempNode = NULL;

	while (*(pStr + index) != '\0') {
		int intValue = convertCharToInt(*(pStr + index)); //���λ��ֵ
		Node *pNode = (Node *) malloc(sizeof(Node)); //����Node�ڶ��ϵ��ڴ�ռ�
		pNode->data = intValue;
		pNode->prior = NULL;
		pNode->next = NULL;
		tempLink->tail = pNode; //����βָ��

		if (index == 0) //ͷ���
				{
			tempLink->head = pNode;

			pTempNode = tempLink->head;
		} else {
			//��ͷ���
			pTempNode->next = pNode;
			pNode->prior = pTempNode;

			pTempNode = pNode;
		} //end if

		index++;
	} //end while
	tempLink->length = index;

	*link = tempLink;

	return SUCCESS;
}

//�ͷ�Link�ṹ��
int releaseLink(Link *pLink) {
	//printf("pLink->length = %d\n",pLink->length);
	if (pLink == NULL || pLink->length <= 0) {
		printf("error link is NULL \n");
		return SUCCESS;
	}

	Node *p = pLink->head;
	//�ͷŽ���ڴ�
	while (p != NULL) {
		free(p);
		p = p->next;
	} //end while
	free(pLink);

	return SUCCESS;
}

//��ӡLink�ṹ��
void printLink(Link *pLink) {
	if (pLink == NULL || pLink->length <= 0) {
		//printf("%s  %d",__FILE__,__LINE__);
		printf("Link is NULL or length = 0\n");
		return;
	}

	Node *p = pLink->head;

	while (p != NULL) {
		printf("%d", p->data);
		p = p->next;
	} //end while
	printf("\n");
}

//�����������
void printReorder(Link *pLink) {
	if (pLink == NULL || pLink->length <= 0) {
		//printf("%s  %d",__FILE__,__LINE__);
		printf("Link is NULL or length = 0\n");
		return;
	}
	Node *p = pLink->tail;
	while (p != NULL) {
		printf("%d", p->data);
		p = p->prior;
	} //end while
	printf("\n");
}

//��Linkת��Ϊchar *
int converLinkToChar(Link *pLink, char **out) {
	Node *p = pLink->head;
	int index = 0;

	char *tempOut = (char *) malloc((pLink->length + 1) * sizeof(char));

	if (tempOut == NULL) {
		printf("error malloc temp\n");
		return ERROR;
	}

	while (p != NULL) {
		tempOut[index] = convertIntToChar(p->data);
		p = p->next;
		index++;
	} //end while
	*(tempOut + index) = '\0';

	*out = tempOut;

	return SUCCESS;
}

char *add(const char *add1, const char *add2) {
	Link *pLinkNum1 = NULL; //��������1
	Link *pLinkNum2 = NULL;
	Link *pResultNum = NULL; //���

	createLink(add1, &pLinkNum1);
	createLink(add2, &pLinkNum2);
	//����
	addNum(pLinkNum1, pLinkNum2, &pResultNum);

	char *ret = NULL;
	converLinkToChar(pResultNum, &ret);

	//releaseLink(pLinkNum1);
	//releaseLink(pLinkNum2);
	//releaseLink(pResultNum);

	return ret;
}

JNIEXPORT jstring JNICALL Java_com_xinlan_addnum_Native_addNum(JNIEnv *env,
		jclass clazz, jstring addNum1Str, jstring addNum2Str) {
	const char *add1String = (*env)->GetStringUTFChars(env, addNum1Str, 0);
	const char *add2String = (*env)->GetStringUTFChars(env, addNum2Str, 0);

	char *result = add(add1String, add2String);
	return (*env)->NewStringUTF(env, result);
}

