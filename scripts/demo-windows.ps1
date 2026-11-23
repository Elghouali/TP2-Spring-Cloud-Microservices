# Run after starting the six Spring Boot services or Docker Compose.
$base = 'http://localhost:8080'
$customers = Invoke-RestMethod "$base/api/customers"
$products = Invoke-RestMethod "$base/api/products"
$firstProduct = $products[0]
$body = @{customerId = $customers[0].id; items = @(@{productId=$firstProduct.id; quantity=2})} | ConvertTo-Json -Depth 6
$bill = Invoke-RestMethod "$base/api/bills" -Method POST -ContentType 'application/json' -Body $body
$bill | ConvertTo-Json -Depth 8
Start-Process 'http://localhost:4200'
