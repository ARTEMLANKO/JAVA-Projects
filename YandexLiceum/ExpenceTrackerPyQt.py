import sys
import csv
from PyQt5.QtMultimedia import QSound
from PyQt5.QtWidgets import QApplication, QWidget, QPushButton, QLineEdit, QLabel, QTextEdit
from PyQt5.QtGui import *
from datetime import datetime
from datetime import date
name = ''
ex = ''
ex1 = ''
ex2 = ''
ex3 = ''
ex4 = ''
ex5 = ''
ex6 = ''
ex7 = ''
ex8 = ''
kkkategory = ''
login = ''
password = ''
newevent = ''
check = ''
sw = ''
sm = ''
name = ''
class Example(QWidget):

    def __init__(self):
        super(Example, self).__init__()

        self.setGeometry(200, 200, 250, 170)
        self.setWindowTitle("Вход")

        self.login = QLabel(self)
        self.login.setGeometry(10, -8, 200, 60)
        self.login.setText('Введите логин:')

        self.vvodlogina = QLineEdit(self)
        self.vvodlogina.setGeometry(10, 31, 100, 30)

        self.password = QLabel(self)
        self.password.setGeometry(10, 38, 200, 60)
        self.password.setText('Введите пароль:')

        self.vvodpassword = QLineEdit(self)
        self.vvodpassword.setGeometry(10, 77, 100, 30)

        self.voiti = QPushButton('Войти', self)
        self.voiti.setGeometry(10, 112, 90, 30)
        self.voiti.clicked.connect(self.vhod)


        self.registracia = QPushButton('Зарегистрироваться', self)
        self.registracia.setGeometry(100, 112, 120, 30)
        self.registracia.clicked.connect(self.registration)

    def vhod(self):
        global name
        global ex
        global login
        global password
        print(1)
        with open("csv2.csv", newline='') as csvfile:
            reader = csv.DictReader(csvfile, delimiter = ';')
            self.t = False
            print(11)
            for row in reader:
                print(23)
                if row['login'] == self.vvodlogina.text() and  row['password'] == self.vvodpassword.text():
                    print('p')
                    login = self.vvodlogina.text()
                    print('p')
                    password = self.vvodpassword.text()
                    print(1)
                    name = row['name']
                    self.t = True
            print(self.t)
            if self.t:
                print('ppp')
                ex.close()
                global ex1
                self.ex1 = Checker()
                ex1 = self.ex1
                self.ex1.show()
            else:
                global ex2
                self.ex2 = Mistake()
                ex2 = self.ex2
                self.ex2.show()

    def registration(self):
        global ex3
        self.ex3 = REGISTRACIA()
        ex3 = self.ex3
        self.ex3.show()

class Checker(QWidget):
    def __init__(self):
        super().__init__()
        global name
        global login
        global password
        self.current_date = str(datetime.now().date())
        self.pokcurrent_date = QLabel(self)
        self.pokcurrent_date.setGeometry(5, -20, 200, 60)
        self.pokcurrent_date.setText(self.current_date[8:10] + '.' + self.current_date[5:7] + '.' + self.current_date[:4])

        self.setGeometry(200, 200, 352, 300)
        self.setWindowTitle("Контроллер расходов")
        self.mistake = QLabel(self)
        self.mistake.setGeometry(45, -2, 213, 60)
        self.mistake.setText('Последние расходы:')

        self.sname = QLabel(self)
        self.sname.setGeometry(237, -19, 200, 60)
        self.sname.setText(name)

        self.events = QTextEdit(self)
        self.events.setGeometry(10, 42, 180, 150)

        self.vihod = QPushButton('Выйти', self)
        self.vihod.setGeometry(279, 1, 60, 20)
        self.vihod.clicked.connect(self.out)

        self.week = QLabel(self)
        self.week.setGeometry(5, 170, 300, 60)

        self.month = QLabel(self)
        self.month.setGeometry(5, 185, 300, 60)
        self.newstr = []
        with open("csv.csv", newline='') as csvfile:
            reader = csv.DictReader(csvfile, delimiter = ';')
            self.t = False
            for row in reader:
                if row['login'] == login and row['password'] == password:
                    self.newstr.append([row['date2'], row['event'], row['sum']])
        self.newstr.sort()
        self.newstr = self.newstr[::-1]
        self.textstr = ''
        for i in self.newstr:
            self.textstr += i[1]
            self.textstr += ', '
            if int(i[2]) % 10 == 1:
                self.textstr += i[2]
                self.textstr += ' рубль'
            elif int(i[2]) % 10 in [5, 6, 7, 8, 9, 0]:
                self.textstr += i[2]
                self.textstr += ' рублей'
            else:
                self.textstr += i[2]
                self.textstr += ' рубля'
            self.textstr += ', '
            self.textstr += i[0]
            self.textstr += '\n'

        self.events.setText(self.textstr)

        self.dobavit = QPushButton('Добавить\nсобытие', self)
        self.dobavit.setGeometry(215, 41, 102, 32)
        self.dobavit.clicked.connect(self.add)

        self.delete = QPushButton('Отредатировать\n события', self)
        self.delete.setGeometry(215, 76, 102, 32)
        self.delete.clicked.connect(self.udalit)

        self.kategoriia = QLabel(self)
        self.kategoriia.setGeometry(217, 100, 122, 32)
        self.kategoriia.setText('Введите категорию:')

        self.vkategoriia = QLineEdit(self)
        self.vkategoriia.setGeometry(215, 127, 110, 28)

        self.searchkategoriia = QPushButton('Найти', self)
        self.searchkategoriia.setGeometry(240, 160, 60, 32)
        self.searchkategoriia.clicked.connect(self.kategory)

    def kategory(self):
        self.t = 0
        global kkkategory
        global login
        global password
        with open("csv.csv", newline='') as csvfile:
            reader = csv.DictReader(csvfile, delimiter=';')
            for row in reader:
                if row['login'] == login and row['password'] == password and row['event'] == self.vkategoriia.text():
                    kkkategory = self.vkategoriia.text()
                    self.t = 1
        if self.t == 0:
            self.ex7 = NoKategory()
            global ex7
            ex7 = self.ex7
            self.ex7.show()
        else:
            global ex6
            self.ex6 = Kategory()
            ex6 = self.ex6
            self.ex6.show()

    def add(self):
        global newevent
        self.newevent = NewEvent()
        newevent = self.newevent
        self.newevent.show()

    def udalit(self):
        global ex3
        self.ex3 = Udalenie()
        ex3 = self.ex3
        self.ex3.show()

    def out(self):
        global ex1
        global ex
        global ex8
        ex8 = Vihod()
        ex8.show()

class Vihod(QWidget):
    def __init__(self):
        super().__init__()
        self.setGeometry(275, 260, 200, 100)
        self.label = QLabel(self)
        self.setWindowTitle(" ")
        self.label.setGeometry(31, -40, 200, 130)
        self.label.setText('Точно ли вы хотите выйти?')
        self.yes = QPushButton('Да', self)
        self.yes.setGeometry(37, 40, 60, 32)
        self.yes.clicked.connect(self.YES)

        self.no = QPushButton('Нет', self)
        self.no.setGeometry(102, 40, 60, 32)
        self.no.clicked.connect(self.NO)

    def YES(self):
        global ex8
        global ex
        global ex1
        ex8.close()
        ex1.close()
        ex.show()

    def NO(self):
        global ex8
        ex8.close()

class NoKategory(QWidget):
    def __init__(self):
        super().__init__()
        self.setGeometry(275, 280, 200, 50)
        self.label = QLabel(self)
        self.setWindowTitle(" ")
        self.label.setGeometry(47, -40, 200, 130)
        self.label.setText('Такой категории нет')

class Kategory(QWidget):
    def __init__(self):
        global ex1
        super().__init__()
        ex1.close()
        self.current_date = str(datetime.now().date())
        self.setGeometry(200, 200, 300, 300)
        global kkkategory
        global login
        global password
        self.setWindowTitle('Планировщик')
        self.label = QLabel(self)
        self.label.setGeometry(22, -40, 250, 130)
        self.labelmonth = QLabel(self)
        self.labelmonth.setGeometry(22, -20, 250, 130)
        self.events = QTextEdit(self)
        self.events.setGeometry(22, 60, 180, 150)
        self.newstr = []
        self.btn = QPushButton('Вернуться назад', self)
        self.btn.setGeometry(22, 214, 110, 30)
        self.btn.clicked.connect(self.back)
        global sm
        global sw
        sm = 0
        sw = 0
        with open("csv.csv", newline='') as csvfile:
            reader = csv.DictReader(csvfile, delimiter=';')
            for row in reader:
                if row['login'] == login and row['password'] == password and row['event'] == kkkategory:
                    self.newstr.append([row['date1'], row['sum']])
        self.newstr.sort()
        self.textstr = ''
        for i in self.newstr:
            self.ddd1 = date(int(self.current_date[:4]), int(self.current_date[5:7]), int(self.current_date[-2:]))
            self.ddd2 = date(int(i[0][:4]), int(i[0][5:7]), int(i[0][-2:]))
            self.delta = self.ddd1 - self.ddd2
            self.delta = str(self.delta)
            if ' ' not in self.delta:
                self.delta = 0
            else:
                self.delta = self.delta.split(' ')
                self.delta = int(self.delta[0])
            if self.delta <= 7:
                sw += int(i[1])
            if self.delta <= 30:
                sm += int(i[1])
            if int(i[1]) % 10 == 1:
                self.textstr += i[1]
                self.textstr += ' рубль'
            elif int(i[1]) % 10 in [5, 6, 7, 8, 9, 0]:
                self.textstr += i[1]
                self.textstr += ' рублей'
            else:
                self.textstr += i[1]
                self.textstr += ' рубля'
            self.textstr += ', '
            self.textstr += i[0]
            self.textstr += '\n'
        if sw % 10 == 1:
            self.label.setText('За последнюю неделю потрачено ' + str(sw) + ' рубль')
        elif sw % 10 in [5, 6, 7, 8, 9, 0] or 10 <= sw % 100 <= 20:
            self.label.setText('За последнюю неделю потрачено ' + str(sw) + ' рублей')
        else:
            self.label.setText('За последнюю неделю потрачено ' + str(sw) + ' рубля')

        if sm % 10 == 1:
            self.labelmonth.setText('За последний месяц потрачено ' + str(sm) + ' рубль')
        elif sm % 10 in [5, 6, 7, 8, 9, 0] or 10 <= sm % 100 <= 20:
            self.labelmonth.setText('За последний месяц потрачено ' + str(sm) + ' рублей')
        else:
            self.labelmonth.setText('За последний месяц потрачено ' + str(sm) + ' рубля')

        self.events.setText(self.textstr)

    def back(self):
        global ex6
        ex6.close()
        global ex1
        ex1.show()

class Udalenie(QWidget):
    def __init__(self):
        super().__init__()
        global ex1
        ex1.close()
        global login
        global password
        self.setGeometry(200, 200, 300, 350)
        self.setWindowTitle("Контроллер расходов")
        self.label = QLabel(self)
        self.label.setGeometry(22, -40, 200, 130)
        self.label.setText('   Удалите или отредактируте\n                   события')
        self.events = QTextEdit(self)
        self.events.setGeometry(17, 43, 180, 250)
        self.btn = QPushButton('Сохранить изменения', self)
        self.btn.setGeometry(40, 294, 130, 30)
        self.btn.clicked.connect(self.save)
        self.newstr = []
        self.another = []
        with open("csv.csv", newline='') as csvfile:
            reader = csv.DictReader(csvfile, delimiter=';')
            self.t = False
            for row in reader:
                if row['login'] == login and row['password'] == password:
                    self.newstr.append([row['date2'], row['event'], row['sum']])
                else:
                    self.another.append([row['login'], row['password'], row['event'], row['sum'], row['date1'], row['date2']])
        self.newstr.sort()
        self.newstr = self.newstr[::-1]
        self.textstr = ''
        for i in self.newstr:
            self.textstr += i[1]
            self.textstr += ', '
            if int(i[2]) % 10 == 1:
                self.textstr += i[2]
                self.textstr += ' рубль'
            else:
                self.textstr += i[2]
                self.textstr += ' рублей'
            self.textstr += ', '
            self.textstr += i[0]
            self.textstr += '\n'
        self.events.setText(self.textstr)

    def save(self):
        global login
        global password
        with open('csv.csv', mode = 'w', newline='') as csvfile:
            writer = csv.writer(
                csvfile, delimiter=';', quotechar='"', quoting=csv.QUOTE_MINIMAL)
            writer.writerow(['login', 'password', 'event', 'sum', 'date1', 'date2'])
            for row in self.another:
                writer.writerow([row[0], row[1], row[2], row[3], row[4], row[5]])
        self.new1 = self.events.toPlainText()
        self.new1 = self.new1.split('\n')
        for i in range(len(self.new1)):
            self.new1[i] = self.new1[i].split(', ')
        self.new1 = self.new1[:-1]
        with open('csv.csv', mode = 'a', newline='') as csvfile:
            writer = csv.writer(
                csvfile, delimiter=';', quotechar='"', quoting=csv.QUOTE_MINIMAL)
            for row in self.new1:
                writer.writerow([login, password, row[0], row[1][:row[1].index(' ')], row[2][6:] + '-' + row[2][3:5] + '-' + row[2][:2], row[2]])
        self.ex1 = Checker()
        global ex3
        ex3.close()
        global ex1
        ex1 = self.ex1
        self.ex1.show()

class NewEvent(QWidget):
    def __init__(self):
        super().__init__()
        global ex1
        ex1.close()
        self.setGeometry(265, 240, 220, 100)
        self.setWindowTitle(" ")
        self.kat = QLabel(self)
        self.kat.setGeometry(5, -8, 70, 60)
        self.kat.setText('  Введите\n  категорию:')

        self.vkat = QLineEdit(self)
        self.vkat.setGeometry(7, 35, 60, 20)

        self.sum = QLabel(self)
        self.sum.setGeometry(100, -10, 87, 60)
        self.sum.setText('Введите сумму\n        (руб.):')

        self.vsum = QLineEdit(self)
        self.vsum.setGeometry(100, 35, 78, 20)

        self.btn = QPushButton('Добавить событие', self)
        self.btn.setGeometry(50, 60, 110, 30)
        self.btn.clicked.connect(self.add)

    def add(self):
        global login
        global password
        global ex1
        self.data = str(datetime.now().date())
        with open('csv.csv', 'a', newline='') as csvfile:
            writer = csv.writer(
                csvfile, delimiter=';', quotechar='"', quoting=csv.QUOTE_MINIMAL)
            print(self.data, 'aaaaaaa')
            writer.writerow([login, password, self.vkat.text(), self.vsum.text(), self.data,  self.data[8:10] + '.' + self.data[5:7] + '.' + self.data[:4]])
        newevent.close()
        global ex1
        ex1 = Checker()
        ex1.show()


class Mistake(QWidget):
    def __init__(self):
        super().__init__()

        self.setGeometry(225, 242, 200, 90)
        self.setWindowTitle(" ")
        self.mistake = QLabel(self)
        self.mistake.setGeometry(35, 7, 200, 60)
        self.mistake.setText('Вы ввели неправильный\n      логин или пароль')

class REGISTRACIA(QWidget):
    def __init__(self):
        super().__init__()
        print(11111)
        self.setGeometry(200, 200, 250, 250)
        self.setWindowTitle("Регистрация")

        self.login = QLabel(self)
        self.login.setGeometry(10, -8, 200, 60)
        self.login.setText('Придумайте логин:')

        self.vvodlogina = QLineEdit(self)
        self.vvodlogina.setGeometry(10, 31, 100, 30)

        self.password = QLabel(self)
        self.password.setGeometry(10, 37, 200, 60)
        self.password.setText('Придумайте пароль:')

        self.vvodpassword = QLineEdit(self)
        self.vvodpassword.setGeometry(10, 76, 100, 30)

        self.newname = QLabel(self)
        self.newname.setGeometry(10, 87, 200, 60)
        self.newname.setText('Придумайте имя:')

        self.vvodnewname = QLineEdit(self)
        self.vvodnewname.setGeometry(10, 126, 100, 30)

        self.registracia = QPushButton('Зарегистрироваться', self)
        self.registracia.setGeometry(10, 162, 120, 30)
        self.registracia.clicked.connect(self.newuser)

    def newuser(self):
        self.uspeh = QLabel(self)
        self.uspeh.setGeometry(10, 300, 200, 60)
        self.uspeh.setText('Вы успешно зарегистрированы')
        ex3.close()
        self.newlogin = self.vvodlogina.text()
        self.newpassword = self.vvodpassword.text()
        self.nnewname = self.vvodnewname.text()
        print([self.newlogin, self.newpassword, self.nnewname])
        with open('csv2.csv', 'a', newline='') as csvfile:
            writer = csv.writer(
                csvfile, delimiter=';', quotechar='"', quoting=csv.QUOTE_MINIMAL)
            writer.writerow([self.newlogin, self.newpassword, self.nnewname])


if __name__ == '__main__':
    app = QApplication(sys.argv)
    ex = Example()
    ex.show()
    sys.exit(app.exec())
