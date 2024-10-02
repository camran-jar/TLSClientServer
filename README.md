# TLSClientServer
## Part 1: TLS Client and Server Implementation Using Java Sockets and SSL APIs (60%)

### The process of generating the root CA certificate and issuing the server certificate.

### Step 1: Create a Root CA Certificate 
* Generate a private key for the Root CA 

`openssl genpkey -algorithm RSA -out rootca.key -aes256`
  * Terminal will prompt for your preferred password 
* Generate the Root CA Certificate

`openssl req -x509 -new -key rootca.key -sha256 -days 365 -out rootca.crt`
  * Terminal will prompt for the following
    * password
    * Country Name
    * State Name
    * Locality Name
    * Organization Name
    * Organizational Unit Name
    * Common Name
    * Email address
  * This prompt will generate a self-signed Root CA Certificate which is valid for 1 year 

### Step 2: Generate the Server Certificate 
  * Generate a private key for the server

`openssl genpkey -algorithm RSA -out server.key -aes256`
  * Terminal will prompt for your preferred password 
* Generate a certificate signing request (CSR)

`openssl req -new -key server.key -out server.csr`
  * Terminal will prompt for the following
    * password
    * Country Name
    * State Name
    * Locality Name
    * Organization Name
    * Organizational Unit Name
    * Common Name
    * Email address
  * Terminal will generate a CSR for the server, which is self signed by the Root CA 

* Sign the CSR with the Root CA to issue the server certificate

`openssl x509 -req -in server.csr -CA rootca.crt -CAkey rootca.key -CAcreateserial -out server.crt -days 365 -sha256 -passin pass:passwd`
  * This command uses the Root CA's private key, to sign the CSR and create a server certificate, which is valid for 1 year

### Step 3: Package the Server Certificate and Key into a Keystore 
To be used in the Java server, the server's private key and certificate in a PKCS12 keystore
* Package the server certificate and private key into a .p12 file 

`openssl pkcs12 -export -in server.crt -inkey server.key -out server.p12 -name server -passin pass:passwd -passout pass:passwd -CAfile rootca.crt -caname rootca -chain`