package com.vndirect;

def build() {
  sh "echo 'build'"
}
def test(name) {
  sh "echo ${name}"
}

return this