package service;

import models.ws.QueryPendingTransactionsRequest;
import models.ws.QueryPendingTransactionsResponse;

public interface PendingTransactionsQueryWebService {
    QueryPendingTransactionsResponse pendingTransactionsQuery (QueryPendingTransactionsRequest criteria);
}
