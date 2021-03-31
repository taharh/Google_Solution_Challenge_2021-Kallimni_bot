# Kallimni Bot - Psychologist App

![Header](https://github.com/taharh/Google_Solution_Challenge_2021-Kallimni_bot/blob/main/images/thumbnail.png?raw=true)
     You can find our demo pitch video here [Demo Video](https://youtu.be/g-tw0YFqrEI)

## Who are we? <img src="https://raw.githubusercontent.com/MartinHeinz/MartinHeinz/master/wave.gif" width="30px">
>Hi , we are kallimni team and we are a group of software engineering students .We are from Morocco.You can find us on linkedin .
![Linkedin: TahaRehah](https://img.shields.io/badge/-TahaRehah-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/taharehah/)
![Linkedin: IsmailYahyaoui](https://img.shields.io/badge/-ismailyahyaoui-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/ismail-yahyaoui-58979717a/)
![Linkedin: AminaElKhalfaoui](https://img.shields.io/badge/-AminaElKhalfaoui-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/amina-el-khalfaoui-10b7411ba/)
![Linkedin: NizarStitou](https://img.shields.io/badge/-nizarst-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/nizarst/)
![Linkedin: IhsaneSardi](https://img.shields.io/badge/-IhsaneSardi-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/ihsane-sardi-a1104615a/)

## What's Kallimni?
* Our app’s goal is to introduce people to the world of psychological treatment, and familiarize the act of visiting a psychologist. 
* Kallimni app takes you to an individual therapy session with our virtual psychologist KALLIMNIBOT . You’ll be free to express your feelings, and talk about all your life struggles.

## Technologies:
![](https://img.shields.io/badge/Platform-Google_Cloud-informational?style=flat&logo=Google-Cloud&logoColor=white&color=2bbc8a)
![](https://img.shields.io/badge/Platform-Android-informational?style=flat&logo=Android&logoColor=white&color=2bbc8a)
![](https://img.shields.io/badge/Library-TensorFlow-informational?style=flat&logo=TensorFlow&logoColor=white&color=2bbc8a)
![](https://img.shields.io/badge/Languages-Python-informational?style=flat&logo=Python&logoColor=white&color=2bbc8a)
![](https://img.shields.io/badge/Languages-Java-informational?style=flat&logo=Java&logoColor=white&color=2bbc8a)
![](https://img.shields.io/badge/Languages-Kotlin-informational?style=flat&logo=Kotlin&logoColor=white&color=2bbc8a)

# **Solution Architecture**
### **1. Cloud Pub/Sub**
![Cloud Pub/Sub](https://github.com/taharh/Google_Solution_Challenge_2021-Kallimni_bot/blob/main/images/Cloudpubsub.png?raw=true)
>*The first one is the Google Cloud Pub/Sub that we’ll be using because
the chatbot messaging feature requires using real time messaging.
An app user creates and sends audio messages to a topic. 
Subscriber two create a subscription to our topic to receive messages from it.
 Subscriber one receives the audio coded in  base sixty four  and transmits it to the NLP model using a cloud trigger.*

 ```python
 python pubsub.py $PROJECT sub_one
 ```
 
### **2. Machine Learning (NLP)**
 ![Cloud Pub/Sub](https://github.com/taharh/Google_Solution_Challenge_2021-Kallimni_bot/blob/main/images/model.png?raw=true)
 >*Here our model extract the emotions from every user’s voice, using an open source model that takes an audio format as an input and predicts 5 different emotions for each gender depending on the frequency of their voice , that way we will be able to communicate with every user no matter what language they’re using. And on every user voice record we feed another model with the extracted emotions so that he keeps training until he predicts the user's psychological illnesses.*

**Model used: https://github.com/MITESHPUTHRANNEU/Speech-Emotion-Analyzer** 

### **3. Android App (MVVM)**
 ![Cloud Pub/Sub](https://github.com/taharh/Google_Solution_Challenge_2021-Kallimni_bot/blob/main/images/mvvm.png?raw=true)
 >*We used the MVVM pattern because it’s scalable and our model needs and audio and a message received and our view model contains a GoogleCloudDataService method that implements the second subscriber*
### **4. Full Architecture**
 ![Cloud Pub/Sub](https://github.com/taharh/Google_Solution_Challenge_2021-Kallimni_bot/blob/main/images/general.png?raw=true)
 >*Our app is based on a general architecture divided on three 
sub-architectures.*



