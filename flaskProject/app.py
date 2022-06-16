from __future__ import division

from flask import Flask, render_template, request

app = Flask(__name__)


@app.route('/')
def form():
    return render_template('form.html')


@app.route('/', methods=['POST'])
def form_post():
    number = int(request.form['number'])
    return str(number / 1.2)


if __name__ == '__main__':
    app.run()
