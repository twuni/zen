Overview
--------

Zen is a transport layer protocol. It supports fragmentation, validation, and domain-based routing. Zen aims to 
succeed TCP/IP, UDP and DNS. Unlike TCP and UDP, Zen does not locate services by ports. Instead, it delegates to
the operating system to detect the contained protocol and delegate to local protocol handlers. Unlike IP, Zen
relies on domain-based routing. Every device in a particular subnet will have a unique domain name, though a
single device may have many vanity domains.

This Java version of Zen is for reference only. It runs on too high of a level to be of practical use, but 
it is intended to demonstrate the concepts that should be adopted in a low-level implementation.
