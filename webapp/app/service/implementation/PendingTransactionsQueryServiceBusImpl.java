package service.implementation;

import models.ws.QueryPendingTransactionsRequest;
import models.ws.QueryPendingTransactionsResponse;
import play.modules.guice.InjectSupport;
import service.PendingTransactionsQueryWebService;
import service.JsonService;


import javax.inject.Inject;

@InjectSupport
public class PendingTransactionsQueryServiceBusImpl extends ExternalJsonAbstractService implements PendingTransactionsQueryWebService {

    private QueryPendingTransactionsRequest pendingTransactionsQueryRequest;
    private QueryPendingTransactionsResponse pendingTransactionsQueryResponse;

    @Inject
    public PendingTransactionsQueryServiceBusImpl(JsonService jsonService) {
        super(jsonService);
    }

    @Override
    public QueryPendingTransactionsResponse pendingTransactionsQuery (QueryPendingTransactionsRequest criteria) {
        this.pendingTransactionsQueryRequest = criteria;
        callServiceBus();
        return pendingTransactionsQueryResponse;
    }

    @Override
    protected String getEndpoint() {
        return "/query/pendingTransactions";
    }

    @Override
    protected Object getReqObject() {
        return pendingTransactionsQueryRequest;
    }

    @Override
    protected void setResObject(Object object) {
        this.pendingTransactionsQueryResponse = (QueryPendingTransactionsResponse) object;
    }

    @Override
    protected Class getResClass() {
        return QueryPendingTransactionsResponse.class;
    }

}
