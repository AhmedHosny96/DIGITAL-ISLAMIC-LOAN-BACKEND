package com.sahay.config;


import com.sahay.cbs.*;
import com.sahay.exception.ApiException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import javax.xml.bind.JAXBElement;
import java.util.UUID;

import static com.sahay.cbs.CcRequest.*;

//@Service
public class CbsClient extends WebServiceGatewaySupport {


    public final String reference = UUID.randomUUID().toString();

    //TODO : CHECK IF ACCOUNT EXISTS

    public QueryDepositAccountResponse getCBSAccount(AcQuery acQuery) throws ApiException {

        ObjectFactory objectFactory = new ObjectFactory();

        QueryDepositAccount queryAccount = new QueryDepositAccount();
        queryAccount.setQuery(acQuery);

        JAXBElement<QueryDepositAccount> queryDepositAccountElement = objectFactory.createQueryDepositAccount(queryAccount);
        try {
            JAXBElement<QueryDepositAccountResponse> response = (JAXBElement<QueryDepositAccountResponse>) getWebServiceTemplate().marshalSendAndReceive(queryDepositAccountElement);

            return response.getValue();
        } catch (Exception e) {
            throw new ApiException(e.getCause().toString());
        }
    }

    // TODO : CHECK BALANCE
    public QueryDepositBalanceResponse getAccountBalance(AcQuery acQuery) throws ApiException {

        ObjectFactory objectFactory = new ObjectFactory();

        QueryDepositBalance balanceQuery = new QueryDepositBalance();
        balanceQuery.setRequest(acQuery);

        JAXBElement<QueryDepositBalance> queryDepositAccountElement = objectFactory.createQueryDepositBalance(balanceQuery);
        try {

            JAXBElement<QueryDepositBalanceResponse> response = (JAXBElement<QueryDepositBalanceResponse>) getWebServiceTemplate().marshalSendAndReceive(queryDepositAccountElement);

            return response.getValue();
        } catch (Exception e) {
            throw new ApiException(e.getCause().toString());
        }
    }

    // GL TO GL
    public PostGLToGLTransferResponse postGlToGl(TxRequest request) throws ApiException {
        
        ObjectFactory objectFactory = new ObjectFactory();
        PostGLToGLTransfer postGLToGLTransfer = new PostGLToGLTransfer();

        postGLToGLTransfer.setRequest(request);

        JAXBElement<PostGLToGLTransfer> postGLToGLTransfer1 = objectFactory.createPostGLToGLTransfer(postGLToGLTransfer);

        try {

            JAXBElement<PostGLToGLTransferResponse> response = (JAXBElement<PostGLToGLTransferResponse>) getWebServiceTemplate().marshalSendAndReceive(postGLToGLTransfer1);

            return response.getValue();
        } catch (Exception e) {
            throw new ApiException(e.getCause().toString());
        }

    }


}
