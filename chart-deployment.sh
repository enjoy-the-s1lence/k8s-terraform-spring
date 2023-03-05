
### build the docker images on minikube
cd client-service
mvn package
docker build -t client-service .
cd ../server-service
mvn package
docker build -t server-service .
cd ..


helm install --set aws.key=AKIAQNL5HWS6ORH5BGZV --set aws.secretkey=TODO -f server-service/chart-values.yaml server-service chart/ping-pong-chart
helm install --set aws.key=AKIAQNL5HWS6ORH5BGZV --set aws.secretkey=TODO -f client-service/chart-values.yaml client-service chart/ping-pong-chart
