import pygame
import csv
from random import randint

if __name__ == '__main__':
    with open("csv.csv", newline='') as csvfile:
        reader = csv.DictReader(csvfile, delimiter=';')
        for row in reader:
            RECORD = row['r']
            print(RECORD)
    with open('csv.csv', mode='w', newline='') as csvfile:
        writer = csv.writer(
            csvfile, delimiter=';', quotechar='"', quoting=csv.QUOTE_MINIMAL)
        writer.writerow(['r'])
        writer.writerow([RECORD])

    pygame.init()
    screen = pygame.display.set_mode((500, 500))
    pygame.display.set_caption('Змейка')
    screen.fill((137, 239, 253))
    font = pygame.font.SysFont('comicsansm', 64)
    font10 = pygame.font.SysFont('comicsansm', 32)
    title = font.render("Змейка", True, (0, 128, 0), (137, 239, 253))
    rec1 = font10.render("Рекорд: " + str(RECORD), True, (255, 0, 0), (137, 239, 253))
    play = pygame.image.load("play3.png")
    restart = pygame.image.load("restart.png")
    menu = pygame.image.load("menu.png")
    running = True
    screen.blit(title, (170, 10))
    screen.blit(play, (170, 60))
    screen.blit(rec1, (370, 10))
    pygame.display.update()
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
                pygame.quit()
            elif event.type == pygame.MOUSEBUTTONDOWN:
                x, y = event.pos
                if (170 <= x <= 330 and 60 <= y <= 220):
                    running = False
    clock = pygame.time.Clock()
    kolp = 0
    tekx = randint(0, 21)
    teky = randint(0, 21)
    tekapplex = randint(0, 21)
    tekappley = randint(0, 21)
    napr = 0
    l = 1
    while tekx != tekapplex and teky != tekappley:
        tekapplex = randint(0, 21)
        tekapplexy = randint(0, 21)
    coords = [(teky, tekx)]
    running = True
    pygame.draw.rect(screen, (255, 255, 0), (30 + tekapplex * 20, 30 + tekappley * 20, 20, 20), 0)
    pygame.display.update()
    screen.fill((0, 255, 0))
    cv = [[0] * 22 for i in range(22)]
    coun = 0
    for i in range(22):
        for j in range(22):
            if coun % 2 == 0:
                pygame.draw.rect(screen, (0, 200, 0), (30 + j * 20, 30 + i * 20, 20, 20), 0)
            else:
                pygame.draw.rect(screen, (0, 255, 0), (30 + j * 20, 30 + i * 20, 20, 20), 0)
            coun += 1
        coun += 1
    record = 0

    while running:
        kolp += 1
        if kolp >= 900000:
            kolp = 0
            if tekx == tekapplex and teky == tekappley:
                tekapplex = randint(0, 21)
                tekappley = randint(0, 21)
                pygame.draw.rect(screen, (255, 255, 0), (30 + tekapplex * 20, 130 + tekappley * 20, 20, 20), 0)
                l += 1
                record += 1
            elif tekx < 0 or tekx > 21 or teky < 0 or teky > 21 or l != len(set(coords[-l:])):
                recc = font.render("ВАШ РЕЗУЛЬТАТ: " + str(record), True, (255, 0, 0))
                if record > int(RECORD):
                    RECORD = str(record)
                with open('csv.csv', mode='w', newline='') as csvfile:
                    writer = csv.writer(
                        csvfile, delimiter=';', quotechar='"', quoting=csv.QUOTE_MINIMAL)
                    writer.writerow(['r'])
                    writer.writerow([RECORD])

                screen.blit(recc, (30, 80))
                screen.blit(restart, (75, 120))
                screen.blit(menu, (250, 120))
                running1 = True
                pygame.display.update()
                while running1:
                    for event in pygame.event.get():
                        if event.type == pygame.QUIT:
                            pygame.quit()
                        if event.type == pygame.MOUSEBUTTONDOWN:
                            x, y = event.pos
                            if 75 <= x <= 235 and 120 <= y <= 280:
                                kolp = 0
                                tekx = randint(0, 21)
                                teky = randint(0, 21)
                                tekapplex = randint(0, 21)
                                tekappley = randint(0, 21)
                                napr = 0
                                l = 1
                                while tekx != tekapplex and teky != tekappley:
                                    tekapplex = randint(0, 21)
                                    tekapplexy = randint(0, 21)
                                coords = [(teky, tekx)]
                                running = True
                                pygame.draw.rect(screen, (255, 255, 0),
                                                 (30 + tekapplex * 20, 30 + tekappley * 20, 20, 20), 0)
                                pygame.display.update()
                                screen.fill((0, 255, 0))
                                cv = [[0] * 22 for i in range(22)]
                                coun = 0
                                for i in range(22):
                                    for j in range(22):
                                        if coun % 2 == 0:
                                            pygame.draw.rect(screen, (0, 200, 0), (30 + j * 20, 30 + i * 20, 20, 20), 0)
                                        else:
                                            pygame.draw.rect(screen, (0, 255, 0), (30 + j * 20, 30 + i * 20, 20, 20), 0)
                                        coun += 1
                                    coun += 1
                                record = 0
                                running1 = False
                            elif 250 <= x <= 410 and 120 <= y <= 280:
                                screen.fill((137, 239, 253))
                                running2 = True
                                screen.blit(title, (170, 10))
                                screen.blit(play, (170, 60))
                                rec1 = font10.render("Рекорд: " + str(RECORD), True, (255, 0, 0), (137, 239, 253))
                                screen.blit(rec1, (370, 10))
                                pygame.display.update()
                                while running2:
                                    for event in pygame.event.get():
                                        if event.type == pygame.QUIT:
                                            pygame.quit()
                                        elif event.type == pygame.MOUSEBUTTONDOWN:
                                            x, y = event.pos
                                            if 170 <= x <= 330 and 60 <= y <= 220:
                                                running2 = False
                                kolp = 0
                                tekx = randint(0, 21)
                                teky = randint(0, 21)
                                tekapplex = randint(0, 21)
                                tekappley = randint(0, 21)
                                napr = 0
                                l = 1

                                while tekx != tekapplex and teky != tekappley:
                                    tekapplex = randint(0, 21)
                                    tekapplexy = randint(0, 21)
                                coords = [(teky, tekx)]
                                running = True
                                pygame.draw.rect(screen, (255, 255, 0),
                                                 (30 + tekapplex * 20, 30 + tekappley * 20, 20, 20), 0)
                                pygame.display.update()
                                screen.fill((0, 255, 0))
                                cv = [[0] * 22 for i in range(22)]
                                coun = 0
                                for i in range(22):
                                    for j in range(22):
                                        if coun % 2 == 0:
                                            pygame.draw.rect(screen, (0, 200, 0), (30 + j * 20, 30 + i * 20, 20, 20), 0)
                                        else:
                                            pygame.draw.rect(screen, (0, 255, 0), (30 + j * 20, 30 + i * 20, 20, 20), 0)
                                        coun += 1
                                    coun += 1
                                record = 0
                                running1 = False
            else:
                screen.fill((0, 128, 0))
                font1 = pygame.font.SysFont('comicsansm', 35)
                title1 = font1.render(str(record), True, (200, 0, 0), (0, 128, 0))
                screen.blit(title1, (460, 4))

                for i in range(22):
                    for j in range(22):
                        if coun % 2 == 0:
                            pygame.draw.rect(screen, (0, 200, 0), (30 + j * 20, 30 + i * 20, 20, 20), 0)
                        else:
                            pygame.draw.rect(screen, (0, 255, 0), (30 + j * 20, 30 + i * 20, 20, 20), 0)
                        coun += 1
                    coun += 1
                pygame.draw.rect(screen, (255, 255, 0), (30 + tekapplex * 20, 30 + tekappley * 20, 20, 20), 0)
                if napr == 'u':
                    teky -= 1
                    coords.append((teky, tekx))
                if napr == 'd':
                    teky += 1
                    coords.append((teky, tekx))
                if napr == 'l':
                    tekx -= 1
                    coords.append((teky, tekx))
                if napr == 'r':
                    tekx += 1
                    coords.append((teky, tekx))
                for i in coords[-l:]:
                    pygame.draw.rect(screen, (255, 0, 0), (30 + i[1] * 20, 30 + i[0] * 20, 20, 20), 0)
                for event in pygame.event.get():
                    if event.type == pygame.QUIT:
                        pygame.quit()
                    if event.type == pygame.KEYDOWN:
                        if event.key == pygame.K_LEFT:
                            napr = 'l'
                        if event.key == pygame.K_RIGHT:
                            napr = 'r'
                        if event.key == pygame.K_UP:
                            napr = 'u'
                        if event.key == pygame.K_DOWN:
                            napr = 'd'
                pygame.display.update()
