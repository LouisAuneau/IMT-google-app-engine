# IMT-google-app-engine
Java contact app using GCP tools such as Google App Engine, Datastorage etc...

## Requirements
- JDK 7
- Maven

## Deployment
In local :
```
 mvn appengine:run
```

In production in Google Cloud Platform :
First, setup your Google Cloud Platform account.
```
gcloud auth login
gcloud config set project imt-2017-11
gcloud config set app/promote_by_default false
```

Then deploy the datastore index :
```
mvn -Dapp.deploy.version={YOUR_VERSION} appengine:deployIndex
```

And finally deploy the application :
```
mvn -Dapp.deploy.version={YOUR_VERSION} appengine:deploy
```
