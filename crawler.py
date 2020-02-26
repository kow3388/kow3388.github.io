import requests
import time
from bs4 import BeautifulSoup
import os
import re
import urllib.request
import json


PTT = 'https://www.ptt.cc'          #一開始網址，方便後面抓取

def get_webpage(url):               #取得網頁內容
    time.sleep(0.5)
    result = requests.get(url = url,cookies={'over18': '1'}) #取得網址若有年齡限制選同意
    if result.status_code != 200:             #網頁取得不正確
        print('Invalid url:', result.url)
        return None
    else:
        return result.text

def get_content(res, date):          #取得網頁內容(輸入原始內文和日期)
    soup = BeautifulSoup(res, 'html.parser')   #用beautifulsoup整理
    
    paging_div = soup.find('div', 'btn-group btn-group-paging')     #上一頁
    prev_url = paging_div.find_all('a')[1]['href']
    
    content = []           #用來儲存內容
    divs = soup.find_all('div', 'r-ent')       #所有有r-ent的文章
    for d in divs:
        if d.find('div', 'date').string.strip() == date:  # 發文日期正確
            push_count = 0
            if d.find('div', 'nrec').string:
                try:
                    push_count = int(d.find('div', 'nrec').string)    #文章推文數
                except ValueError:
                    pass


            if d.find('a'):                    #有連結
                href = d.find('a')['href']     #連結網址
                title = d.find('a').string     #標題
                content.append({'title': title, 
                                'href': href, 
                                'push_count': push_count})
    return content, prev_url


def save_picture(img_urls, title):      #儲存圖片
    if img_urls:
        root= 'C:/Users/asus/Desktop/ptt/'      #在此資料夾下創建
        try:
            name = title.strip()
            name = name.replace(':',' ')
            dname = os.path.join(root, name)
            os.makedirs(dname)
            for img_url in img_urls:            #將格式一致
                if img_url.split('//')[1].startswith('m.'):
                    img_url = img_url.replace('//m.', '//i.')
                if not img_url.split('//')[1].startswith('i.'):
                    img_url = img_url.split('//')[0] + '//i.' + img_url.split('//')[1]
                if not img_url.endswith('.jpg'):
                    img_url += '.jpg'
                fname = img_url.split('/')[-1]
                urllib.request.urlretrieve(img_url, os.path.join(dname, fname))    #儲存圖片
        except Exception as e:
            print(e)

def parse(res):
    soup = BeautifulSoup(res, 'html.parser')
    links = soup.find(id='main-content').find_all('a')
    img_urls = []           #儲存連結
    for link in links:
        if re.match(r'^https?://(i.)?(m.)?imgur.com', link['href']):
            img_urls.append(link['href'])
    return img_urls

def save_content(res, title):       #將文章內容和推文寫入檔案
    soup = BeautifulSoup(res, 'html.parser')
    root = 'C:/Users/asus/Desktop/ptt/'
    txt = 'content.txt'
    try:
        main_content =  soup.find('div', id='main-content').text    #文章內的主要內容
        name = title.strip()
        name = name.replace(':',' ')
        dname = os.path.join(root, name)
        if not os.path.exists(root):
            os.makedirs(dname)
        fname = os.path.join(dname, txt)
        file = open(fname, 'w', encoding='UTF-8')
        file.write(main_content)
        file.close()
        print("文件保存成功")
    except Exception as e:
        print(e)
    return main_content


def main():
    current_page = get_webpage(PTT + '/bbs/Beauty/index.html')
    root = 'C:/Users/asus/Desktop/ptt/'
    if not os.path.exists(root):
        os.makedirs(root)
    if current_page:
        articles = []      #全部文章
        date = time.strftime("%m/%d").lstrip('0')       #日期
        current_articles, prev_url = get_content(current_page, date)
        while current_articles:        #這頁有文章
            articles += current_articles       #加入articles
            current_page = get_webpage(PTT + prev_url)     #到上一頁
            current_articles, prev_url = get_content(current_page, date)

    for article in articles:
        print(article)        #印出標題等等
        page = get_webpage(PTT + article['href'])      #進入文章
        if page:
            img_urls = parse(page)
            save_picture(img_urls, article['title'])
            content = save_content(page, article['title'])
            content = content.split('\n')
            article['num_image'] = len(img_urls)     #計算這篇文章有幾張圖片
            article['content'] = content

    name = 'data.json'
    fjson = os.path.join(root, name)
    with open(fjson, 'w', encoding='utf-8') as f:       #儲存文章資訊
            json.dump(articles, f, indent=2, sort_keys=True, ensure_ascii=False)

main()
