package com.example.demo.request;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "�[�J�ʪ���")
public class addCartReq {
	// �ʪ����s��
	// String cartNumber;
	// �Ȥ�W��
	String customer;
	// �ӫ~�s��
	String productId;
	// �ӫ~
	String productName;
	// ������B
	String unitPrice;
}
