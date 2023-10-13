# Vault with Groovy




```bash



# https://github.com/jenkinsci/docker/blob/master/README.md#usage


# Generate a SSH key pair for the jenkins master/slave setup
ssh-keygen -t rsa -f sensitive/jenkins_agent_1

# Set this variable for the docker-compose file
export JENKINS_AGENT_SSH_PUBKEY=$(cat sensitive/jenkins_agent_1.pub)

# Bring Up Infrastructure, run in Background, based upon this file path
docker-compose -f ./docker/docker-compose.yaml up -d

# Login to Jenkins & get the password
docker exec -it jenkins /bin/sh -c "cat /var/jenkins_home/secrets/initialAdminPassword"

# Open Jenkins Credentials under the System Global Domain
open http://localhost:8080/manage/credentials/store/system/domain/_/

# Create a System Level credential for secret string in Jenkins for Vault App Role ID
open http://localhost:8080/manage/credentials/store/system/domain/_/newCredentials
vault_app_role_id

# Create a System Level credential for secret string in Jenkins for Vault App Role Secret ID
open http://localhost:8080/manage/credentials/store/system/domain/_/newCredentials
vault_app_role_secret_id

# Create a System Level credential for SSH key in Jenkins for jenkins agent
cat sensitive/jenkins_agent_1
jenkins

# Install the http request plugin:
open http://localhost:8080/manage/pluginManager/available

# Create a new Job for Pipeline & call it ZTS
open http://localhost:8080/view/all/newJob
zts

# Under Pipeline Script, copy & paste vault.groovy
cat vault.groovy

# Remember, Vault hostname is hardcoded into this example (hint: you can change that logic)

# CleanUp
docker-compose -f ./docker/docker-compose.yaml down
docker system prune -f
docker volume prune -f
docker-compose -f ./docker/docker-compose.yaml up -d
```
