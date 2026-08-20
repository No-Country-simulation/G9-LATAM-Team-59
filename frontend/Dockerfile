FROM nginx:alpine

# Copiar la configuracion personalizada de NGINX con el reverse proxy
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copiar los archivos estaticos del frontend (html, css, js, imagenes)
COPY . /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
