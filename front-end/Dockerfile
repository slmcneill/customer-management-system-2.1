FROM node:20-alpine AS build
WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci
COPY . .

ARG VITE_CUSTOMER_URL=http://backend:8080/api/customers
ARG VITE_AUTH_URL=http://localhost:8081/account

RUN npm run build
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
RUN chmod -R a+r /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
