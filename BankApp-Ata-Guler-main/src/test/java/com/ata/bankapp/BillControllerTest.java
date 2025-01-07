package com.ata.bankapp;

import com.ata.bankapp.client.BillClient;
import com.ata.bankapp.controller.BillController;
import com.ata.bankapp.model.Bill;
import com.ata.bankapp.model.BillStatus;
import com.ata.bankapp.request.BillPaymentRequest;
import com.ata.bankapp.service.BillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@WebMvcTest(BillController.class)
public class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillService billService;

    @MockBean
    private BillClient billClient;

    private Bill bill;
    private BillPaymentRequest billPaymentRequest;
    private List<Bill> billList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        bill = new Bill();
        bill.setId(1L);
        bill.setAccountId(1L);
        bill.setAmount(100.0);
        bill.setDueDate(LocalDate.now());
        bill.setStatus(BillStatus.PENDING);

        billPaymentRequest = new BillPaymentRequest();
        billPaymentRequest.setBillId(1L);
        billPaymentRequest.setAccountId(1L);
        billPaymentRequest.setAmount(100.0);

        billList = Arrays.asList(bill);
    }

    @Test
    public void testCreateBill() throws Exception {
        given(billClient.createBill(bill)).willReturn(bill);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/bills/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountId\":1,\"amount\":100.0,\"dueDate\":\"2024-08-12\",\"status\":\"PENDING\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    public void testPayBill() throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Payment successful");
        given(billClient.payBill(billPaymentRequest)).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/bills/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"billId\":1,\"accountId\":1,\"amount\":100.0}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Payment successful"));
    }

    @Test
    public void testGetBillById() throws Exception {
        given(billClient.getBillById(1L)).willReturn(bill);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bills/{billId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    public void testGetAllBills() throws Exception {
        given(billClient.getAllBills()).willReturn(billList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bills")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L));
    }

    @Test
    public void testMarkOverdueBills() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/bills/markOverdue"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(billClient).markOverdueBills();
    }
}
