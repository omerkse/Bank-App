package com.ata.bankapp;

import com.ata.bankapp.model.Transfer;
import com.ata.bankapp.repo.TransferRepository;
import com.ata.bankapp.service.TransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

public class TransferServiceImplTest {

    @InjectMocks
    private TransferServiceImpl transferService;

    @Mock
    private TransferRepository transferRepository;

    private Transfer transfer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        transfer = new Transfer();
        transfer.setId(1L);
        transfer.setFromAccountId(1L);
        transfer.setToAccountId(2L);
        transfer.setAmount(500.0);
    }

    @Test
    public void testCreateTransfer() {
        given(transferRepository.save(transfer)).willReturn(transfer);

        transferService.createTransfer(transfer);

        verify(transferRepository, times(1)).save(transfer);
    }

    @Test
    public void testGetTransferById() {
        given(transferRepository.findById(1L)).willReturn(Optional.of(transfer));

        Transfer result = transferService.getTransferById(1L);

        assertNotNull(result);
        assertEquals(transfer.getId(), result.getId());
        verify(transferRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTransferById_NotFound() {
        given(transferRepository.findById(1L)).willReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            transferService.getTransferById(1L);
        });

        assertEquals("Transfer not found", thrown.getMessage());
        verify(transferRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllTransfers() {
        given(transferRepository.findAll()).willReturn(Arrays.asList(transfer));

        List<Transfer> transfers = transferService.getAllTransfers();

        assertNotNull(transfers);
        assertEquals(1, transfers.size());
        assertEquals(transfer.getId(), transfers.get(0).getId());
        verify(transferRepository, times(1)).findAll();
    }
}
