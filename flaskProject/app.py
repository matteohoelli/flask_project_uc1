from __future__ import division
from flask import Flask, render_template, request, redirect
from flask_mqtt import Mqtt
import paho.mqtt.client as mqtt

app = Flask(__name__)

client = mqtt.Client('test')



@app.route('/')
def log():
    return render_template('log.html')


logged = 0


@app.route('/', methods=['POST'])
def log_post():
    if request.form['email'] == "admin@gmail.com" and request.form['password'] == "admin":
        global logged
        logged = 1
        return redirect("http://127.0.0.1:5000/rech")
    else:
        logged = 0
        return redirect("http://127.0.0.1:5000/")


@app.route('/rech')
def form():
    if logged:
        return render_template('form.html')
    else:
        return redirect("http://127.0.0.1:5000/")


@app.route('/rech', methods=['POST'])
def form_post():
    number = int(request.form['number'])
    if request.form.get('netto'):
        client.publish("lohn", "Netto~" + str(number) + " / 1.2 =" + str(int(number / 1.2)))
        return '''<h1 style="text-align: center; padding-top: 300px">Netto~ {} €</h1>'''.format(str(int(number / 1.2)))
    else:
        client.publish("lohn", "Brutto~" + str(number) + " * 1.2 =" + str(int(number * 1.2)))
        return '''<h1 style="text-align: center; padding-top: 300px">Brutto~ {} €</h1>'''.format(str(int(number * 1.2)))


def on_connect(rc):
    if rc == 0:
        print("Connection successfull")
    else:
        print("Connection failed")


if __name__ == '__main__':
    app.run()
    client.host = "localhost"
    client.connect("localhost", port=2222)
    client.username = "mattno"
    client.password = "mattno"
