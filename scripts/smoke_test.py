#!/usr/bin/env python3
"""Smoke test after all Spring services are up, using only Python stdlib."""
import json,time,urllib.request,urllib.error,sys
BASE='http://localhost:8080'
def request(path,method='GET',body=None):
    data=json.dumps(body).encode() if body is not None else None
    req=urllib.request.Request(BASE+path,data=data,headers={'Content-Type':'application/json'},method=method)
    with urllib.request.urlopen(req,timeout=8) as response:
        return json.loads(response.read().decode())

def wait(path):
    for attempt in range(25):
        try:return request(path)
        except (urllib.error.URLError,TimeoutError,ValueError):
            time.sleep(3)
    raise RuntimeError('Service unavailable at '+BASE+path+'; check Eureka, Docker logs and Gateway profile')

if __name__=='__main__':
    try:
        customers=wait('/api/customers');products=wait('/api/products')
        if not customers or not products:raise RuntimeError('No demo customers/products found')
        p=products[0]; price=float(p['price']);qty=2
        if p['quantity']<qty:raise RuntimeError('Insufficient stock for demo product')
        bill=request('/api/bills','POST',{'customerId':customers[0]['id'],'items':[{'productId':p['id'],'quantity':qty}]})
        expected=round(price*qty,2)
        assert round(float(bill['total']),2)==expected,(bill['total'],expected)
        assert bill['customerId']==customers[0]['id']
        assert len(bill['items'])==1
        assert request('/api/bills/'+str(bill['id']))['id']==bill['id']
        print('PASS: Customer REST, Inventory REST, Gateway, Feign Billing; Bill ID',bill['id'],'total',bill['total'])
    except Exception as e:
        print('FAILED:',e,file=sys.stderr)
        sys.exit(1)
