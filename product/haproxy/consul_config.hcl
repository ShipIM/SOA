consul {
  address = "localhost:8500"
}

template {
  source = "/haproxy.conf.tmpl"
  destination = "/etc/haproxy/haproxy.cfg"
  command = "service haproxy reload"
}
