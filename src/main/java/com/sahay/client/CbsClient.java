package com.sahay.client;


import com.sahay.cbs.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import javax.xml.bind.JAXBElement;
import java.util.UUID;

import static com.sahay.cbs.CcRequest.*;

//@Service
@Slf4j
public class CbsClient extends WebServiceGatewaySupport {

    public final String reference = UUID.randomUUID().toString();
    public final Long customerTypeId = 701l;
    public final String region = "SOMALI";
    public final Long relationShipOfficerId = 882l;


    //TODO : Customer creation

    public CreateCustomerResponse createCustomerInfo(CcRequest request) throws Exception {


        request.setReference(reference);
        request.setCustTypeId(customerTypeId);
        request.setRegion(region);
        request.setRelOfficer(relationShipOfficerId);

        CcContact contact = new CcContact();
        contact.setType(237l);
        contact.setValue(request.getMobileNumber());

        Contacts contacts = new Contacts();
        contacts.getContact().add(contact);

        request.setContacts(contacts);

        CreateCustomer customer = new CreateCustomer();
        customer.setRequest(request);

        JAXBElement<CreateCustomer> customerPayload = new ObjectFactory().createCreateCustomer(customer);

        log.info("CUSTOMER PAYLOAD : {}", customer);

        try {
            JAXBElement<CreateCustomerResponse> response = (JAXBElement<CreateCustomerResponse>) getWebServiceTemplate()
                    .marshalSendAndReceive(customerPayload);
            return response.getValue();

        } catch (Exception e) {

            log.info("FAILED TO CREATE CUSTOMER: {}" , e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    //TODO: Deposit account creation

    public CreateDepositAccountResponse createCustomerDepositAccount(CaRequest request) throws Exception {

        CreateDepositAccount depositAccount = new CreateDepositAccount();
        depositAccount.setRequest(request);

        JAXBElement<CreateDepositAccount> depositAccountPayload = new ObjectFactory().createCreateDepositAccount(depositAccount);

        try {
           JAXBElement<CreateDepositAccountResponse> response = (JAXBElement<CreateDepositAccountResponse>) getWebServiceTemplate().marshalSendAndReceive(depositAccountPayload);
           return response.getValue();
        }
        catch (Exception e) {
            log.info("FAILED TO CREATE CUSTOMER: {}" , e.getMessage());
            throw new Exception(e.getMessage());
        }

    }
}
