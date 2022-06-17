from __future__ import division

from flask import Flask, render_template, request

app = Flask(__name__)


@app.route('/')
def form():
    return render_template('form.html')


@app.route('/', methods=['POST'])
def form_post():
    number = int(request.form['number'])
    if request.form.get('netto'):

        return '''<h1 style="text-align: center; padding-top: 300px">Netto~ {} €</h1>'''.format(str(int(number / 1.2)))
    else:
        return '''<h1 style="text-align: center; padding-top: 300px">Brutto~ {} €</h1>'''.format(str(int(number * 1.2)))


if __name__ == '__main__':
    app.run()
