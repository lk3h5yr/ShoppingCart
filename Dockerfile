# java版本
FROM java:11

# 將本地文件掛載到當前容器
VOLUME /tmp

# 把自己的項目加到app.jar裡面可以隨便取
ADD gclient.jar app.jar
# 運行中創建一個app.jar文件
RUN bash -c 'touch /app.jar'

# 開放port號
EXPOSE 8761

# ENTRYPOINT指定容器運行後默認
ENTRYPOINT ["java","-jar","/app.jar"]