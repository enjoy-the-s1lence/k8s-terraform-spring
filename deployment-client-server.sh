### set docker env
eval $(minikube docker-env)

### build the docker images on minikube
cd client-service
mvn package
docker build -t client-service .
cd ../server-service
mvn package
docker build -t server-service .
cd ..


kubectl delete -f k8s/secret.yaml
kubectl create -f k8s/secret.yaml

kubectl delete -f k8s/rbac.yaml
kubectl create -f k8s/rbac.yaml

kubectl delete -f k8s/server-service-deployment.yaml
kubectl create -f k8s/server-service-deployment.yaml

kubectl delete -f k8s/client-service-deployment.yaml
kubectl create -f k8s/client-service-deployment.yaml

# Check that the pods are running
kubectl get pods
