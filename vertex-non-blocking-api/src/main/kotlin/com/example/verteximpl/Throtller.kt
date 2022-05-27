package com.example.verteximpl

import java.util.concurrent.atomic.AtomicInteger

class Throtller {
    var incoming = AtomicInteger()
    var outgoing = AtomicInteger()
}