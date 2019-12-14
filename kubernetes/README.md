
# Instalacion de Cluster de Kubernetes
## Requisitos:
-Servidor Centos7 
-Docker
-Git

## 1 Desahabilitar Swap
swapoff -a

## 2 Editar /etc/fstab
vim /etc/fstab

## 3 Comentar la siguiente linea en el fstab sobre el out swap
/root/swap swap swap sw 0 0

## 4 Agregar el repositorio de Kubernetes
cat << EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://packages.cloud.google.com/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://packages.cloud.google.com/yum/doc/yum-key.gpg https://packages.cloud.google.com/yum/doc/rpm-package-key.gpg
exclude=kube*
EOF

## 5 Desahabilitar SELinux
setenforce 0

## 6 Desahabilitar Permanentemente el SELinux
vim /etc/selinux/config

## 7 Cambiar el enforcing a desahabilitado
SELINUX=disabled

## 8 Install Kubernetes 1.11.3
yum install -y kubelet-1.11.3 kubeadm-1.11.3 kubectl-1.11.3 kubernetes-cni-0.6.0 --disableexcludes=kubernetes

## 9 Iniciar y habilitar el servicio de Kubernetes
systemctl start kubelet && systemctl enable kubelet

## 10 Crear el archivo k8s.conf copiando y pegando el siguiente script en consola
cat << EOF >  /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
#Ejecutar el siguiente comando
sysctl --system

# -----Hasta aqui se tiene que replicar tambien en los workers

## 11 Crear kube-config.yml
vim kube-config.yml

## 12 Agregar la siguiente configuracion al kube-config.yml
apiVersion: kubeadm.k8s.io/v1alpha1
kind:
kubernetesVersion: "v1.11.3"
networking:
  podSubnet: 10.244.0.0/16
apiServerExtraArgs:
  service-node-port-range: 8000-31274

## 13 Inicializar Kubernetes y COPIAR EL TOKEN
kubeadm init --config kube-config.yml

## 14 Copiar admin.conf al directorio HOME
mkdir -p $HOME/.kube  

cp -i /etc/kubernetes/admin.conf $HOME/.kube/config

chown $(id -u):$(id -g) $HOME/.kube/config

# -------FLANNEL-----
## 15 Instalar Flannel
k3ubectl apply -f https://raw.githubusercontent.com/coreos/flannel/v0.9.1/Documentation/kube-flannel.yml

## 16 Instalar parche de Flannel
vi /etc/kubernetes/manifests/kube-controller-manager.yaml

### Agregar lo siguiente al kube-controller.yaml en /etc/kubernetes/manifests/kube-controller-manger.yaml solo en el caso de el archivo no lo tenga

--allocate-node-cidrs=true <br> 
--cluster-cidr=10.244.0.0/16

## Reiniciar Kubelet
systemctl restart kubelet

### -----------ESTE PASO SIGUIENTE SOLO ES PARA LOS WORKERS TOMANDO LA INFORMACION DEL PASO 13 INICIO -> 
## 19 Usar el join tokedn para agregar al worker al nodo cluster
kubeadm join < MASTER_IP_PRIVATE >:6443 --token < TOKEN > --discovery-token-ca-cert-hash sha256:< HASH >
### ----------FIN----------

## 20 Ejecutar en el nodo maestro lo siguiente para ver si se agrego el worker
kubectl get nodes
kubectl get pods -n kube-system

# -------------DESPLEGAR MICROSERVICIO--------
## El MICROSERVICIO esta hecho en node, pero sirve con cualquier microservicio teniendo su docker file configurado
## URL del repositorio
https://github.com/aapiro/content-kubernetes-prometheus-app.git

## 1 Crear imagen de docker

docker build -t rivethead42/comicbox .

## 2 Login en Docker Hub:

docker login

## 3 Hacer Push de imagen a Docker Hub:

docker push < USERNAME >/comicbox

## 4 Crear deployment de la aplicacion en deployment

kubectl apply -f deployment.yml

