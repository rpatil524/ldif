java -server -Xmx2G -Xms256M -Dlogback.configurationFile=ldif-singlemachine\src\main\resources\logback.xml -cp ldif-singlemachine\target\ldif-single-0.3.1-jar-with-dependencies.jar ldif.local.IntegrationJob $*
