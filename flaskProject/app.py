from __future__ import division

from flask import Flask, render_template, request

app = Flask(__name__)


@app.route('/')
def form():
    return render_template('form.html')


number = 0


@app.route('/', methods=['POST'])
def form_post():
    number = request.form['number']
    return number


@app.route('/number')
def calc():
    return int(number) / 1.2


if __name__ == '__main__':
    app.run()
