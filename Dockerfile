# java����
FROM java:11

# �N���a��󱾸����e�e��
VOLUME /tmp

# ��ۤv�����إ[��app.jar�̭��i�H�H�K��
ADD gclient.jar app.jar
# �B�椤�Ыؤ@��app.jar���
RUN bash -c 'touch /app.jar'

# �}��port��
EXPOSE 8761

# ENTRYPOINT���w�e���B����q�{
ENTRYPOINT ["java","-jar","/app.jar"]