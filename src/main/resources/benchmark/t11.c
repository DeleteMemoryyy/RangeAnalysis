int foo()
{
    int sum = -10;

    for (int i = 0; i < 100; ++i)
        for (int j = 0; j <= i; ++j)
            {
                if (i == 99)
                    sum += j * j;
            }
    return sum;
}