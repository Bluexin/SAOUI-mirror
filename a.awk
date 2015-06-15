BEGIN {
    n=split(a,b,"\n")
    for (i=1; i<=n; i++) {
        split(b[i],z,"=")
        w[("\""z[1]"\"")]=("\""z[2]"\"")
    }
    RS="^$" #Read whole file in a single record
}
{
    n=patsplit($0,c,/"[^"]*"[[:blank:]]*:[[:blank:]]*"[^"]*"/,s1)
    printf "%s", s1[0]
    for (i=1; i<=n; i++) {
        patsplit(c[i],d,/"[^"]*"/,s2)
        if (d[1] in w)
            d[2]=w[d[1]]
        printf "%s%s%s%s%s%s",s2[0],d[1],s2[1],d[2],s2[2],s1[i]
    }
}
