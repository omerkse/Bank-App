package com.ata.bankapp;

import com.ata.bankapp.controller.TransferController;
import com.ata.bankapp.model.Transfer;
import com.ata.bankapp.request.TransferRequest;
import com.ata.bankapp.service.TransferService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@WebMvcTest(TransferController.class)
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferService transferService;

    private Transfer transfer;
    private List<Transfer> transferList;
    private TransferRequest transferRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        transfer = new Transfer();
        transfer.setId(1L);
        transfer.setFromAccountId(100L);
        transfer.setToAccountId(200L);
        transfer.setAmount(500.0);

        transferList = Arrays.asList(transfer);

        transferRequest = new TransferRequest();
        transferRequest.setFromAccountId(100L);
        transferRequest.setToAccountId(200L);
        transferRequest.setAmount(500.0);
    }

    @Test
    public void testCreateTransfer() throws Exception {
        willDoNothing().given(transferService).createTransfer(org.mockito.ArgumentMatchers.any(Transfer.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fromAccountId\":100,\"toAccountId\":200,\"amount\":500.0}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Transfer completed successfully."));
    }

    @Test
    public void testGetTransferById() throws Exception {
        given(transferService.getTransferById(1L)).willReturn(transfer);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/transfers/{transferId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }

    @Test
    public void testGetAllTransfers() throws Exception {
        given(transferService.getAllTransfers()).willReturn(transferList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/transfers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L));
    }
}
