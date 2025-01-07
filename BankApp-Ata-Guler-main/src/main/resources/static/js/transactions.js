async function updateTransactions(accountId) {
    try {
        const response = await fetch(`/api/accounts/${accountId}/transactions`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        // Transaction update logic
    } catch (error) {
        console.error('İşlem geçmişi alınırken hata:', error);
    }
}