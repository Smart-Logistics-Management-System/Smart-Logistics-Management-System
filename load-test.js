import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '10s', target: 20 }, // 10 saniyede 20 kullanıcıya çık
    { duration: '30s', target: 50 }, // 30 saniye boyunca 50 kullanıcıda kal
    { duration: '10s', target: 0 },  // 10 saniyede yükü boşalt
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // İsteklerin %95'i 500ms altında olmalı
  },
};

const BASE_URL_USER = 'http://host.docker.internal:8081/api/users';
const BASE_URL_CARGO = 'http://host.docker.internal:8082/api/cargo';

export default function () {
  // 1. Login Testi
  const loginPayload = JSON.stringify({
    email: 'furkan@example.com',
    password: 'password123',
  });
  const loginParams = { headers: { 'Content-Type': 'application/json' } };
  const loginRes = http.post(`${BASE_URL_USER}/login`, loginPayload, loginParams);
  
  check(loginRes, {
    'login başarılı (200)': (r) => r.status === 200,
  });

  sleep(1); // Gerçekçi olması için aralarda bekleme süresi

  // 2. Kargo Listesi Testi (Dashboard yükü)
  const listRes = http.get(BASE_URL_CARGO);
  check(listRes, {
    'kargo listesi başarılı (200)': (r) => r.status === 200,
  });

  sleep(1);

  // 3. Tekil Kargo Sorgulama (UUID ile tracking testi)
  // Not: DB'de olan bir takip numarası kullanmak testi daha gerçekçi kılar
  const trackRes = http.get(`${BASE_URL_CARGO}/test-tracking-123`);
  check(trackRes, {
    'tracking sorgusu cevap verdi': (r) => r.status === 200 || r.status === 404,
  });

  sleep(1);
}
