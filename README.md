# WSO2 Tenant Loading Health Checker

[WSO2 Identity Server](https://wso2.com/identity-server/) supports [multitenancy](https://is.docs.wso2.com/en/latest/administer/introduction-to-multitenancy/) 
with the goal of maximizing resource sharing by allowing multiple users (tenants) to log in and use a single server/cluster at the same time, in a tenant-isolated manner.

When multitenancy is enabled, the tenants are loaded into the memory when serving for each tenant. Two tenant loading 
policies are supported and [Lazy Loading](https://is.docs.wso2.com/en/latest/administer/configuring-the-tenant-loading-policy/#configuring-lazy-loading)
is enabled by default. When a tenant becomes inactive for a long period of time, the tenant is unloaded from the server's memory.

The number of currently loaded tenants is a metric that the admins might require to monitor the health of the deployment.
This custom health checker can be used monitor the number of loaded tenants into the memory at a given time.

## Build

Clone the repository and in the directory where the pom file is located, issue the following command to build the project.
```
mvn clean install
```

## Deploy

After successfully building the project, the resulting jar file can be retrieved from the target directory. 
(the already built jar is included in the release section) copy the resulting jar to the 
<IS_HOME>/repository/components/dropins/ directory.

If you are using WSO2 IS 5.9.0 or a later version, the health check API and this health checker can be enabled by 
adding the following configurations in the deployment.toml.

```
[carbon_health_check]
enable= true

[[health_checker]]
name = "TenantLoadingHealthChecker"
order="86"
```

If you are using WSO2 IS 5.8.0 or an older version, <IS_HOME>/repository/conf/health-check-config.xml file need to be 
updated as below. `Enable` is set to `true` and `TenantLoadingHealthChecker` is added. If you had already enabled 
the CarbonHealthCheckConfigs, you may add the new `TenantLoadingHealthChecker` along with the existing ones.
```dtd
<Server xmlns="http://wso2.org/projects/carbon/carbon.xml">

    <CarbonHealthCheckConfigs>

        <Enable>true</Enable>

        <HealthCheckers>
            <HealthChecker name="TenantLoadingHealthChecker" orderId="86" enable="true"></HealthChecker>
        </HealthCheckers>

    </CarbonHealthCheckConfigs>
</Server>

```
## Testing

Start the server and send a GET request to the health check API

Sample curl request :

```
curl -k -v https://localhost:9443/api/health-check/v1.0/health
```

Response :
```
{
    "health": [
        {
            "key": "loaded.tenants",
            "value": "10"
        }
    ]
}
```
