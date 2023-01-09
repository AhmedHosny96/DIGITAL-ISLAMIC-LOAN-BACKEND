package com.sahay.client;



import com.sahay.cbs.*;
import com.sahay.exception.ApiException;
import com.sahay.exception.PhoneNumberTakenException;
import com.sahay.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import javax.xml.bind.JAXBElement;
import java.util.UUID;

import static com.sahay.cbs.CcRequest.*;

//@Service
public class CbsClient extends WebServiceGatewaySupport {

    @Autowired
    public CustomerRepository customerRepository;

    public final String reference = UUID.randomUUID().toString();
    public final Long customerTypeId = 701l;
    public final Long relationShipOfficerId = 882l;

    //TODO : Customer creation

    public CreateCustomerResponse createCustomerInfo(CcRequest request) throws Exception {


        String customerByContact = customerRepository.findCustomerByContact(request.getMobileNumber());

//        String customerById = customerRepository.findCustomerById(request.getIdentities().)

        if(customerByContact != null){
            throw new PhoneNumberTakenException("Phone number is taken!");
        }

        String idNumber = request.getIdentities().getIdentity().get(0).getIdNumber();

        String customerById = customerRepository.findCustomerById(idNumber);

        if(customerById != null){
            throw new PhoneNumberTakenException("Id number is taken!");
        }

        request.setReference(reference);
        request.setCustTypeId(customerTypeId);
        request.setRelOfficer(relationShipOfficerId);

        var contact = new CcContact();
        contact.setType(237l);
        contact.setValue(request.getMobileNumber());

        var contacts = new Contacts();
        contacts.getContact().add(contact);
        request.setContacts(contacts);
        var customer = new CreateCustomer();
        customer.setRequest(request);

        JAXBElement<CreateCustomer> customerPayload = new ObjectFactory().createCreateCustomer(customer);

        try {
            JAXBElement<CreateCustomerResponse> response = (JAXBElement<CreateCustomerResponse>) getWebServiceTemplate()
                    .marshalSendAndReceive(customerPayload);
            return response.getValue();

        } catch (Exception e) {
            throw new ApiException("CBS Service is unreachable, Please contact your system administrator");
        }
    }

    //TODO: Deposit account creation

    public CreateDepositAccountResponse createCustomerDepositAccount(CaRequest request) throws Exception {

        var depositAccount = new CreateDepositAccount();
        depositAccount.setRequest(request);

        JAXBElement<CreateDepositAccount> depositAccountPayload = new ObjectFactory().createCreateDepositAccount(depositAccount);

        try {
           JAXBElement<CreateDepositAccountResponse> response = (JAXBElement<CreateDepositAccountResponse>) getWebServiceTemplate().marshalSendAndReceive(depositAccountPayload);
           return response.getValue();
        }
        catch (Exception e) {
            throw new ApiException("CBS Service is unreachable, Please contact your system administrator");
        }

    }
}
