import telebot
from telebot import types
from datetime import datetime
from datetime import date
import csv

bot = telebot.TeleBot('6036347571:AAEJ2qPFbp7b_EbizaQlKQ66LBD_H7EB9mk')
MESSAGE = ''
COST = 0
sp = []
sp2 = []

@bot.message_handler(commands=["start"])
def start(message):
    markup = types.InlineKeyboardMarkup(row_width = 1)
    bt1 = types.InlineKeyboardButton('Общая сумма расходов', callback_data = 'bt1')
    bt2 = types.InlineKeyboardButton('Добавить расход', callback_data = 'bt2')
    bt3 = types.InlineKeyboardButton('Поменять расход', callback_data = 'bt3')
    bt4 = types.InlineKeyboardButton('Сбросить расход', callback_data = 'bt4')
    bt5 = types.InlineKeyboardButton('Расходы за последний месяц', callback_data = 'bt5')
    bt6 = types.InlineKeyboardButton('Расходы за последнюю неделю', callback_data = 'bt6')
    bt7 = types.InlineKeyboardButton('Расходы за последний год', callback_data = 'bt7')
    markup.add(bt1, bt2, bt3, bt4, bt6, bt5, bt7)
    bot.send_message(message.chat.id, 'Здравствуйте, Это контроллер расходов.', reply_markup = markup)

@bot.message_handler(content_types = ['text'])
def message1(message):
    if message.text != '/start':
        if '=' in message.text:
            global MESSAGE
            MESSAGE = str(message.text).split('=')
            with open('проект.csv', mode = 'a', newline='') as csvfile:
                writer = csv.writer(
                    csvfile, delimiter=';', quotechar='"', quoting=csv.QUOTE_MINIMAL)
                writer.writerow([str(message.chat.id), str(datetime.now().date()), MESSAGE[0], MESSAGE[1]])
            markup = types.InlineKeyboardMarkup(row_width = 1)
            bt1 = types.InlineKeyboardButton('Общая сумма расходов', callback_data = 'bt1')
            bt2 = types.InlineKeyboardButton('Добавить расход', callback_data = 'bt2')
            bt3 = types.InlineKeyboardButton('Поменять расход', callback_data = 'bt3')
            bt4 = types.InlineKeyboardButton('Сбросить расход', callback_data = 'bt4')
            bt5 = types.InlineKeyboardButton('Расходы за последний месяц', callback_data = 'bt5')
            bt6 = types.InlineKeyboardButton('Расходы за последнюю неделю', callback_data = 'bt6')
            bt7 = types.InlineKeyboardButton('Расходы за последний год', callback_data = 'bt7')
            markup.add(bt1, bt2, bt3, bt4, bt6, bt5, bt7)
            bot.send_message(message.chat.id, 'Это контроллер расходов.', reply_markup = markup)
        else:
            q = message.text
            global sp
            global sp2
            with open('проект.csv', mode='w', newline='') as csvfile:
                writer = csv.writer(
                    csvfile, delimiter=';', quotechar='"', quoting=csv.QUOTE_MINIMAL)
                writer.writerow(['user', 'date', 'event', 'cost'])
                for i in range(len(sp)):
                    if i != int(q) - 1:
                        writer.writerow([str(message.chat.id), sp[i][0], sp[i][1], sp[i][2]])
                for i in sp2:
                    writer.writerow(sp2)
            bot.send_message(message.chat.id, 'Введите категорию и цену (через знак "=")')

@bot.callback_query_handler(func   = lambda call: True)
def callback(call):
    if call.message:
        if call.data == 'bt1':
            s = 0
            with open('проект.csv', newline='') as csvfile:
                reader = csv.DictReader(csvfile, delimiter = ';')
                for row in reader:
                    if str(row['user']) == str(call.message.chat.id):
                        s += int(row['cost'])
            markup = types.InlineKeyboardMarkup(row_width=1)
            bt2 = types.InlineKeyboardButton('Добавить расход', callback_data='bt2')
            bt3 = types.InlineKeyboardButton('Поменять расход', callback_data='bt3')
            bt4 = types.InlineKeyboardButton('Сбросить расход', callback_data='bt4')
            bt5 = types.InlineKeyboardButton('Расходы за последний месяц', callback_data='bt5')
            bt6 = types.InlineKeyboardButton('Расходы за последнюю неделю', callback_data='bt6')
            bt7 = types.InlineKeyboardButton('Расходы за последний год', callback_data='bt7')
            markup.add(bt2, bt3, bt4, bt5, bt6, bt7)
            if s % 10 in [0, 5, 6, 7, 8, 9]:
                bot.send_message(call.message.chat.id, 'Вы потратили ' + str(s) + ' рублей', reply_markup = markup)
            if s % 10 in [2, 3, 4]:
                bot.send_message(call.message.chat.id, 'Вы потратили ' + str(s) + ' рубля', reply_markup = markup)
            if s % 10 in [1]:
                bot.send_message(call.message.chat.id, 'Вы потратили ' + str(s) + ' рубль', reply_markup = markup)
        if call.data == 'bt2':
            current_date = str(datetime.now().date())
            bot.send_message(call.message.chat.id, 'Введите категорию и цену (через знак "=")')
        if call.data == 'bt3':
            global sp
            global sp2
            sp = []
            sp2 = []
            new = ''
            with open('проект.csv', newline='') as csvfile:
                reader = csv.DictReader(csvfile, delimiter = ';')
                for row in reader:
                    if str(row['user']) == str(call.message.chat.id):
                        sp.append([row['date'], row['event'], row['cost']])
                    else:
                        sp2.append([row['user'], row['date'], row['event'], row['cost']])
            for i in range(len(sp)):
                new += str(i+1) + ') '
                new += sp[i][0] + ', '
                new += sp[i][1] + ', '
                new += sp[i][2] + '.'
                new += '\n'
            bot.send_message(call.message.chat.id, 'Какой пункт вы хотите поменять?\n' + str(new))
        if call.data == 'bt4':
            sp = []
            sp2 = []
            new = ''
            with open('проект.csv', newline='') as csvfile:
                reader = csv.DictReader(csvfile, delimiter=';')
                for row in reader:
                    if str(row['user']) == str(call.message.chat.id):
                        sp.append([row['date'], row['event'], row['cost']])
                    else:
                        sp2.append([row['user'], row['date'], row['event'], row['cost']])
            with open('проект.csv', mode='w', newline='') as csvfile:
                writer = csv.writer(
                    csvfile, delimiter=';', quotechar='"', quoting=csv.QUOTE_MINIMAL)
                writer.writerow(['user', 'date', 'event', 'cost'])
                for i in sp2:
                    writer.writerow(sp2)
            markup = types.InlineKeyboardMarkup(row_width=1)
            bt1 = types.InlineKeyboardButton('Общая сумма расходов', callback_data='bt1')
            bt2 = types.InlineKeyboardButton('Добавить расход', callback_data='bt2')
            bt3 = types.InlineKeyboardButton('Поменять расход', callback_data='bt3')
            bt4 = types.InlineKeyboardButton('Сбросить расход', callback_data='bt4')
            bt5 = types.InlineKeyboardButton('Расходы за последний месяц', callback_data='bt5')
            bt6 = types.InlineKeyboardButton('Расходы за последнюю неделю', callback_data='bt6')
            bt7 = types.InlineKeyboardButton('Расходы за последний год', callback_data='bt7')
            markup.add(bt1, bt2, bt3, bt4, bt5, bt6, bt7)
            bot.send_message(call.message.chat.id, 'Данные о ваших покупках удалены', reply_markup = markup)
        if call.data == 'bt5':
            current_date = str(datetime.now().date())
            sp = []
            sp2 = []
            new = ''
            d = dict()
            with open('проект.csv', newline='') as csvfile:
                reader = csv.DictReader(csvfile, delimiter=';')
                for row in reader:
                    if str(row['user']) == str(call.message.chat.id):
                        if str(row['date'][5:7]) == current_date[5:7]:
                            if row['event'] not in d:
                                d[row['event']] = int(row['cost'])
                            else:
                                d[row['event']] += int(row['cost'])
                            sp.append([row['date'], row['event'], row['cost']])
                    else:
                        sp2.append([row['user'], row['date'], row['event'], row['cost']])
            for i in range(len(sp)):
                new += str(i+1) + ') '
                new += str(sp[i][0]) + ', '
                new += str(sp[i][1]) + ', '
                new += str(sp[i][2])
                new += '\n'
            new += 'Итого: \n'
            for i in d:
                if int(d[i]) % 10 in [0, 5, 6, 7, 8, 9]:
                    new += str(i) + ': ' + str(d[i]) + ' рублей\n'
                if int(d[i]) % 10 in [2, 3, 4]:
                    new += str(i) + ': ' + str(d[i]) + ' рубля\n'
                if int(d[i]) % 10 in [1]:
                    new += str(i) + ': ' + str(d[i]) + ' рубль\n'
            markup = types.InlineKeyboardMarkup(row_width=1)
            bt1 = types.InlineKeyboardButton('Общая сумма расходов', callback_data='bt1')
            bt2 = types.InlineKeyboardButton('Добавить расход', callback_data='bt2')
            bt3 = types.InlineKeyboardButton('Поменять расход', callback_data='bt3')
            bt4 = types.InlineKeyboardButton('Сбросить расход', callback_data='bt4')
            bt6 = types.InlineKeyboardButton('Расходы за последнюю неделю', callback_data='bt6')
            bt7 = types.InlineKeyboardButton('Расходы за последний год', callback_data='bt7')
            markup.add(bt1, bt2, bt3, bt4, bt6, bt7)
            bot.send_message(call.message.chat.id, new, reply_markup=markup)
        if call.data == 'bt7':
            current_date = str(datetime.now().date())
            sp = []
            sp2 = []
            new = ''
            d = dict()
            with open('проект.csv', newline='') as csvfile:
                reader = csv.DictReader(csvfile, delimiter=';')
                for row in reader:
                    if str(row['user']) == str(call.message.chat.id):
                        if str(row['date'][:4]) == current_date[:4]:
                            sp.append([row['date'], row['event'], row['cost']])
                            if row['event'] not in d:
                                d[row['event']] = int(row['cost'])
                            else:
                                d[row['event']] += int(row['cost'])
                    else:
                        sp2.append([row['user'], row['date'], row['event'], row['cost']])
            for i in range(len(sp)):
                new += str(i+1) + ') '
                new += str(sp[i][0]) + ', '
                new += str(sp[i][1]) + ', '
                new += str(sp[i][2])
                new += '\n'
            new += 'Итого: \n'
            for i in d:
                if int(d[i]) % 10 in [0, 5, 6, 7, 8, 9]:
                    new += str(i) + ': ' + str(d[i]) + ' рублей\n'
                if int(d[i]) % 10 in [2, 3, 4]:
                    new += str(i) + ': ' + str(d[i]) + ' рубля\n'
                if int(d[i]) % 10 in [1]:
                    new += str(i) + ': ' + str(d[i]) + ' рубль\n'
            markup = types.InlineKeyboardMarkup(row_width=1)
            bt1 = types.InlineKeyboardButton('Общая сумма расходов', callback_data='bt1')
            bt2 = types.InlineKeyboardButton('Добавить расход', callback_data='bt2')
            bt3 = types.InlineKeyboardButton('Поменять расход', callback_data='bt3')
            bt4 = types.InlineKeyboardButton('Сбросить расход', callback_data='bt4')
            bt6 = types.InlineKeyboardButton('Расходы за последнюю неделю', callback_data='bt6')
            bt5 = types.InlineKeyboardButton('Расходы за последний месяц', callback_data='bt5')
            markup.add(bt1, bt2, bt3, bt4, bt6, bt5)
            bot.send_message(call.message.chat.id, new, reply_markup=markup)
        if call.data == 'bt6':
            current_date = str(datetime.now().date())
            sp = []
            sp2 = []
            new = ''
            d = dict()
            with open('проект.csv', newline='') as csvfile:
                reader = csv.DictReader(csvfile, delimiter=';')
                for row in reader:
                    if str(row['user']) == str(call.message.chat.id):
                        if str(row['date'][:4]) == current_date[:4]:
                            if row['event'] not in d:
                                d[row['event']] = int(row['cost'])
                            else:
                                d[row['event']] += int(row['cost'])
                            sp.append([row['date'], row['event'], row['cost']])
                    else:
                        sp2.append([row['user'], row['date'], row['event'], row['cost']])
            for i in range(len(sp)):
                new += str(i+1) + ') '
                new += str(sp[i][0]) + ', '
                new += str(sp[i][1]) + ', '
                new += str(sp[i][2])
                new += '\n'
            new += 'Итого: \n'
            for i in d:
                if int(d[i]) % 10 in [0, 5, 6, 7, 8, 9]:
                    new += str(i) + ': ' + str(d[i]) + ' рублей\n'
                if int(d[i]) % 10 in [2, 3, 4]:
                    new += str(i) + ': ' + str(d[i]) + ' рубля\n'
                if int(d[i]) % 10 in [1]:
                    new += str(i) + ': ' + str(d[i]) + ' рубль\n'
            markup = types.InlineKeyboardMarkup(row_width=1)
            bt1 = types.InlineKeyboardButton('Общая сумма расходов', callback_data='bt1')
            bt2 = types.InlineKeyboardButton('Добавить расход', callback_data='bt2')
            bt3 = types.InlineKeyboardButton('Поменять расход', callback_data='bt3')
            bt4 = types.InlineKeyboardButton('Сбросить расход', callback_data='bt4')
            bt7 = types.InlineKeyboardButton('Расходы за последний год', callback_data='bt7')
            bt5 = types.InlineKeyboardButton('Расходы за последний месяц', callback_data='bt5')
            markup.add(bt1, bt2, bt3, bt4, bt5, bt7)
            bot.send_message(call.message.chat.id, new, reply_markup=markup)
bot.polling(none_stop = True)
